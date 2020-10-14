package com.diga.generic.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;


/**
 * created by TMT
 */
public class PropUtils {

    /**
     * 相对路径名称
     */
    private static final String CLASSPATH_SUFFIX = "classpath:";

    /**
     * 根据流来获取 properties 对象
     *
     * @param is
     * @return
     */
    public static KV load(InputStream is) {
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new KV(properties);
    }

    /**
     * 根据文件来获取 properties 对象
     *
     * @param file
     * @return
     */
    public static KV load(File file) {
        try {
            return load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        throw new IllegalArgumentException("get KV errors, by FileNotFoundException..");
    }

    /**
     * 根据 相对路径 来获取 properties 对象
     *
     * @param classpath
     * @return
     */
    public static KV load(String classpath) {
        if (classpath.contains(CLASSPATH_SUFFIX)) {
            classpath = classpath.substring(CLASSPATH_SUFFIX.length());
        }

        return load(ClassUtils.getClassLoader().getResourceAsStream(classpath));
    }


    public static class KV {
        private Map<String, String> map;

        public KV(Properties properties) {
            this.map = new HashMap<String, String>((Map) properties);
        }

        public String getString(String key, String defaultValue) {
            return MapUtils.getString(map, key, defaultValue);
        }

        public Boolean getBoolean(String key, Boolean defaultValue) {
            return MapUtils.getBoolean(map, key, defaultValue);
        }

        public Long getLong(String key, Long defaultValue) {
            return MapUtils.getLong(map, key, defaultValue);
        }

        public Short getShort(String key, Short defaultValue) {
            return MapUtils.getShort(map, key, defaultValue);
        }

        public Float getFloat(String key, Float defaultValue) {
            return MapUtils.getFloat(map, key, defaultValue);
        }

        public Double getDouble(String key, Double defaultValue) {
            return MapUtils.getDouble(map, key, defaultValue);
        }

        public Byte getByte(String key, Byte defaultValue) {
            return MapUtils.getByte(map, key, defaultValue);
        }

        public Integer getInteger(String key, Integer defaultValue) {
            return MapUtils.getInteger(map, key, defaultValue);
        }


        public String getString(String key) {
            return MapUtils.getString(map, key);
        }

        public Boolean getBoolean(String key) {
            return MapUtils.getBoolean(map, key);
        }

        public Long getLong(String key) {
            return MapUtils.getLong(map, key);
        }

        public Short getShort(String key) {
            return MapUtils.getShort(map, key);
        }

        public Float getFloat(String key) {
            return MapUtils.getFloat(map, key);
        }

        public Double getDouble(String key) {
            return MapUtils.getDouble(map, key);
        }

        public Byte getByte(String key) {
            return MapUtils.getByte(map, key);
        }

        public Integer getInteger(String key) {
            return MapUtils.getInteger(map, key);
        }

        public <T> T get(String key, Class<T> returnClazz) {
            switch (returnClazz.getName()) {
                case "int":
                case "java.lang.Integer":
                    return (T) getInteger(key);

                case "double":
                case "java.lang.Double":
                    return (T) getDouble(key);

                case "float":
                case "java.lang.Float":
                    return (T) getFloat(key);

                case "long":
                case "java.lang.Long":
                    return (T) getLong(key);

                case "short":
                case "java.lang.Short":
                    return (T) getShort(key);

                case "boolean":
                case "java.lang.Boolean":
                    return (T) getBoolean(key);

                case "byte":
                case "java.lang.Byte":
                    return (T) getByte(key);

                case "java.util.Date":
                    return (T) new Date(getLong(key));

                case "java.lang.String":
                    return (T) getString(key);
            }

            return null;
        }

        /**
         * 基于build的cache简单缓存
         */
        private static Map<Class, Object> buildTypeCache = new HashMap();

        public static void clearBuildCache() {
            buildTypeCache.clear();
        }

        /**
         * 将properties中的属性转换为对应的实体类, 转换的实体类必须存在无参构造器
         *
         * @param prefix properties的前缀, 如果没有前缀. 则传入 null
         * @param clazz  实体类类型
         * @param <T>
         * @return 返回转换后的实体类
         */
        public <T> T build(String prefix, Class<T> clazz) {
            Object cache = buildTypeCache.get(clazz);
            // 如果这个类已经被build过了
            if (cache != null) {
                return (T) cache;
            }

            T instance = null;
            try {
                instance = clazz.newInstance();
                buildTypeCache.put(clazz, instance);
                Field[] fields = clazz.getDeclaredFields();

                for (Field f : fields) {
                    String fName = f.getName();
                    if (prefix != null) {
                        fName = prefix + "." + fName;
                    }
                    Class<?> type = f.getType();

                    Object value = null;
                    if (ClassUtils.isValueType(type)) {
                        value = get(fName, type);
                    } else {
                        // 判断这个类是否是集合类型, 不支持 MAP
                        if (Collection.class.isAssignableFrom(type)) {
                            // 1. 取出泛型
                            Type[] types = ReflexUtils.getFieldParameterizedType(f);
                            if (types.length != 1) {
                                throw new IllegalArgumentException("字段: " + f.getName() + "的泛型数量不为1");
                            }
                            Class typeClass = (Class) types[0];
                            value = buildCollection(typeClass);
                            value = CollectionUtils.to((Collection) value, type);
                        } else {
                            value = build(type);
                        }
                    }
                    try {
                        BeanUtils.copyProperty(instance, f.getName(), value);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return instance;
        }

        public <T> Collection<T> buildCollection(Class<T> clazz) {
            String prefix = StringUtils.lowerFirstCase(clazz.getSimpleName());
            Field[] fields = clazz.getDeclaredFields();
            Map<String, T> listMap = new HashMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String k = entry.getKey();

                if(!k.startsWith(prefix)) {
                    continue;
                }

                for (Field f : fields) {
                    String fPrefix = prefix + "." + f.getName() + ".";

                    if (k.startsWith(fPrefix)) {
                        // 后缀
                        String remain = k.substring(fPrefix.length());
                        if (!remain.contains(".")) {
                            T instance = listMap.get(remain);
                            if (instance == null) {
                                instance = ReflexUtils.tryInstance(clazz);
                                if (instance == null) {
                                    throw new IllegalArgumentException("无法实例化类" + clazz.getName());
                                }
                                listMap.put(remain, instance);
                            }

                            try {
                                BeanUtils.copyProperty(instance, f.getName(), get(k, f.getType()));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }


            return listMap.values();
        }


        public <T> T build(Class<T> clazz) {
            return build(StringUtils.lowerFirstCase(clazz.getSimpleName()), clazz);
        }
    }

}
