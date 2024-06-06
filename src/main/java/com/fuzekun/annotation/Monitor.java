package com.fuzekun.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author: Zekun Fu
 * @date: 2024/6/2 17:46
 * @Description: 监控方法的注解
 */
@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Monitor {
}
