package com.zhy.fabridge.compiler;

import com.google.auto.service.AutoService;
import com.zhy.fabridge.annotation.FCallbackId;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by zhy on 16/4/12.
 */
@AutoService(Processor.class)
public class FabridgeProcess extends AbstractProcessor
{
    private Types typeUtils;
    private Elements elementUtils;
    private Messager messager;
    private Map<String, ProxyInfo> mProxyMap = new HashMap<String, ProxyInfo>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);

        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(FCallbackId.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        mProxyMap.clear();
        messager.printMessage(Diagnostic.Kind.NOTE, "process start ==>");


        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(FCallbackId.class))
        {
            //check method valid.
            if (!checkMethodValid(annotatedElement, FCallbackId.class)) return false;

            ExecutableElement annotatedMethod = (ExecutableElement) annotatedElement;

            List<? extends VariableElement> parameters = ((ExecutableElement) annotatedElement).getParameters();
            //遍历方法参数
            messager.printMessage(Diagnostic.Kind.NOTE, "lalalala-->" + parameters.size());

            for (VariableElement e : parameters)
            {
                messager.printMessage(Diagnostic.Kind.NOTE, "lalalala-->" + e.asType().toString());
            }

            //class type
            TypeElement classElement = (TypeElement) annotatedMethod.getEnclosingElement();
            //full class name
            String fqClassName = classElement.getQualifiedName().toString();

            ProxyInfo proxyInfo = mProxyMap.get(fqClassName);
            if (proxyInfo == null)
            {
                proxyInfo = new ProxyInfo(elementUtils, classElement);
                mProxyMap.put(fqClassName, proxyInfo);
            }

            FCallbackId fCallbackId = annotatedMethod.getAnnotation(FCallbackId.class);
            String id = fCallbackId.id();
            proxyInfo.callbackMethods.put(id, annotatedMethod);

        }


        for (String key : mProxyMap.keySet())
        {
            ProxyInfo proxyInfo = mProxyMap.get(key);
            try
            {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        proxyInfo.getProxyClassFullName(),
                        proxyInfo.getTypeElement());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e)
            {
                error(proxyInfo.getTypeElement(),
                        "Unable to write injector for type %s: %s",
                        proxyInfo.getTypeElement(), e.getMessage());
            }

        }
        return true;
    }

    private void error(Element element, String message, Object... args)
    {
        if (args.length > 0)
        {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
    }

    private boolean checkMethodValid(Element annotatedElement, Class clazz)
    {
        if (annotatedElement.getKind() != ElementKind.METHOD)
        {
            error(annotatedElement, "%s must be declared on method.", clazz.getSimpleName());
            return false;
        }
        if (ClassValidator.isPrivate(annotatedElement) || ClassValidator.isAbstract(annotatedElement))
        {
            error(annotatedElement, "%s() must can not be abstract or private.", annotatedElement.getSimpleName());
            return false;
        }

        return true;
    }

}
