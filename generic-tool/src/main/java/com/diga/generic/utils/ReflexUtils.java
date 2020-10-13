package com.diga.generic.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 反射工具包, 为了简化反射操作
 */
public class ReflexUtils {

    /**
     * 判断方法是否为 native 修饰的方法
     *
     * @param method
     * @return
     */
    public static boolean isNative(Method method) {
        return Modifier.isNative(method.getModifiers());
    }

    /**
     * 判断类是否为 native 修饰的类
     *
     * @param beanClass
     * @return
     */
    public static boolean isNative(Class<?> beanClass) {
        return Modifier.isNative(beanClass.getModifiers());
    }


    /**
     * 判断方法是否为 static 修饰的方法
     *
     * @param method
     * @return
     */
    public static boolean isStatic(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * 判断类是否为 static 修饰的类
     *
     * @param beanClass
     * @return
     */
    public static boolean isStatic(Class<?> beanClass) {
        return Modifier.isStatic(beanClass.getModifiers());
    }


    /**
     * 判断类是否为 final 修饰的类
     *
     * @param beanClass
     * @return
     */
    public static boolean isFinal(Class<?> beanClass) {
        return Modifier.isFinal(beanClass.getModifiers());
    }

    /**
     * 判断方法是否为 fianl 修饰的方法
     *
     * @param method
     * @return
     */
    public static boolean isFinal(Method method) {
        return Modifier.isFinal(method.getModifiers());
    }



}
