package com.zhy.fabridge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhy on 16/4/12.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface FCallbackId
{
    String id();
}
