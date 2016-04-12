package com.zhy.fabridge.compiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by zhy on 16/4/12.
 */
public class ProxyInfo
{
    private String packageName;
    private String proxyClassName;
    private TypeElement typeElement;

    Map<String, ExecutableElement> callbackMethods = new HashMap<>();

    public static final String PROXY = "FabridgeProxy";

    public ProxyInfo(Elements elementUtils, TypeElement classElement)
    {
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        //classname
        String className = ClassValidator.getClassName(classElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + "$$" + PROXY;
        this.typeElement = classElement;
    }

    public TypeElement getTypeElement()
    {
        return typeElement;
    }


    public String getProxyClassFullName()
    {
        return packageName + "." + proxyClassName;
    }


    public String generateJavaCode()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import com.zhy.fabridge.lib.*;\n");
        builder.append('\n');

        builder.append("public class ").append(proxyClassName).append(" implements " + ProxyInfo.PROXY + "<" + typeElement.getSimpleName() + ">");
        builder.append(" {\n");

        generateMethods(builder);
        builder.append('\n');

        builder.append("}\n");
        return builder.toString();
    }

    private void generateMethods(StringBuilder builder)
    {
        builder.append("@Override\n ");
        builder.append("public void call(" + typeElement.getSimpleName() + " source , String id, Object... params) {\n");
        for (String id : callbackMethods.keySet())
        {


            builder.append("if(( \"" + id + "\").equals(id))");
            builder.append("\n{");
            String method = callbackMethods.get(id).getSimpleName().toString();
            String params = getParams(callbackMethods.get(id));
            builder.append("source." + method + "("+params+");");
            builder.append("}\n");
        }

        builder.append("  }\n");
    }

    private String getParams(ExecutableElement executableElement)
    {
        StringBuilder sb = new StringBuilder();
        List<? extends VariableElement> params = executableElement.getParameters();
        for (int i = 0; i < params.size(); i++)
        {
            VariableElement param = params.get(i);
            sb.append("(" + param.asType().toString() + ")");
            sb.append("params[" + i + "]");

            if (i != params.size() - 1)
            {
                sb.append(" , ");
            }

        }

        return sb.toString();
    }
}
