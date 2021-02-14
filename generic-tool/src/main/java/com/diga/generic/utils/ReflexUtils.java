package com.diga.generic.utils;

import javax.sql.rowset.serial.SerialBlob;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射工具包, 为了简化反射操作
 */
public class ReflexUtils {
    public static final Map<String, Object> singletonMap = new HashMap();


    /**
     * 获取单例对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getSingletonInstance(Class<T> clazz) {
        String name = clazz.getName();
        Object instance = singletonMap.get(name);
        if (instance != null) {
            return (T) instance;
        }
        synchronized (name) {
            if (instance == null) {
                singletonMap.put(name, tryInstance(clazz));
            }
        }
        return getSingletonInstance(clazz);
    }

    /**
     * 值转换
     *
     * @param value       需要转换的值
     * @param returnClass 转换后的类型
     * @param <T>
     * @return 返回转换后的结果
     */
    public static <T> T conversionValue(Object value, Class<T> returnClass) {
        Object returnValue = value;
        switch (returnClass.getName()) {
            case "int":
            case "java.lang.Integer":
                returnValue = Integer.parseInt(value.toString());
                break;

            case "double":
            case "java.lang.Double":
                returnValue = Double.parseDouble(value.toString());
                break;

            case "float":
            case "java.lang.Float":
                returnValue = Float.parseFloat(value.toString());
                break;

            case "long":
            case "java.lang.Long":
                returnValue = Long.parseLong(value.toString());
                break;

            case "short":
            case "java.lang.Short":
                returnValue = Short.parseShort(value.toString());
                break;

            case "boolean":
            case "java.lang.Boolean":
                returnValue = Boolean.parseBoolean(value.toString());
                break;

            case "byte":
            case "java.lang.Byte":
                returnValue = Byte.parseByte(value.toString());
                break;

            case "java.util.Date":
                break;

            case "java.lang.String":
                returnValue = value.toString();
                break;

            case "java.math.BigDecimal":
                returnValue = new BigDecimal(value.toString());
                break;

            case "java.sql.Blob":
                try {
                    returnValue = new SerialBlob(value.toString().getBytes());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return (T) returnValue;
    }


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
        } catch (InstantiationException | IllegalAccessException e) {
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


    /**
     * 基于反射来进行值的设置
     *
     * @param bean     实例对象
     * @param property 实例对象中的属性
     * @param value    需要设置的值
     */
    public static <T> void set(Object bean, String property, T value) {
        try {
            Field field = bean.getClass().getDeclaredField(property);
            field.setAccessible(true);

            Class<?> type = field.getType();
            Object conversionValue = conversionValue(value, field.getType());
            field.set(bean, conversionValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过字段获取值
     *
     * @param bean
     * @param property
     * @param <T>
     * @return
     */
    public static <T> T get(Object bean, String property) {
        T val = null;
        try {
            Field field = bean.getClass().getDeclaredField(property);
            val = (T) get(bean, property, field.getType());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return val;
    }


    /**
     * 通过字段获取值
     *
     * @param bean
     * @param property
     * @param <T>
     * @return
     */
    public static <T> T get(Object bean, String property, Class<T> returnClazz) {
        T val = null;
        try {
            Field field = bean.getClass().getDeclaredField(property);
            field.setAccessible(true);
            val = (T) field.get(bean);
            val = conversionValue(val, returnClazz);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return val;
    }

    /**
     * 调用Setter方法
     *
     * @param bean
     * @param property
     * @param value
     * @param <T>
     */
    public static <T> void invokeSetter(Object bean, String property, T value) {
        Class beanClass = bean.getClass();
        try {
            Field declaredField = beanClass.getDeclaredField(property);
            Method method = beanClass.getMethod(String.format("%s%s", "set", StringUtils.firstUpper(declaredField.getName())), declaredField.getType());
            Object conversionValue = conversionValue(value, declaredField.getType());
            method.invoke(bean, conversionValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用 Getter 方法
     *
     * @param bean
     * @param property
     * @param <T>
     * @return
     */
    public static <T> T invokeGetter(Object bean, String property) {
        Class beanClass = bean.getClass();
        try {
            Field declaredField = beanClass.getDeclaredField(property);
            Method method = beanClass.getMethod(String.format("%s%s", "get", StringUtils.firstUpper(declaredField.getName())));
            return (T) method.invoke(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
