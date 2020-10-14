package com.diga.generic.utils;

import java.lang.reflect.*;

/**
 * 反射工具包, 为了简化反射操作
 */
public class ReflexUtils {

    /**
     * 尝试实例化类, 失败返回null
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T tryInstance(Class<T> clazz) {
        T val = null;
        try {
            val = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return val;
    }


    /**
     * 获取字段的泛型, 参考 https://blog.csdn.net/sai_simon/article/details/98663284
     */
    public static Type[] getFieldParameterizedType(Field f) {
        ParameterizedType genericType = (ParameterizedType) f.getGenericType();
        Type[] args = genericType.getActualTypeArguments();
        return args;
    }

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


    /**
     * 当前类是否为接口
     *
     * @param beanClass
     * @return
     */
    public static boolean isInterface(Class<?> beanClass) {
        return beanClass.isInterface();
    }

    /**
     * 当前方法是否为接口方法
     *
     * @param method
     * @return
     */
    public static boolean isInterface(Method method) {
        return Modifier.isInterface(method.getModifiers());
    }

    /**
     * 当前类是否为抽象类
     */
    public static boolean isAbstract(Class<?> beanClass) {
        return Modifier.isAbstract(beanClass.getModifiers());
    }

    /**
     * 当前方法是否为抽象方法
     */
    public static boolean isAbstract(Method method) {
        return Modifier.isAbstract(method.getModifiers());
    }
}
