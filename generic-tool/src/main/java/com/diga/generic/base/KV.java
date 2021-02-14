package com.diga.generic.base;

import com.diga.generic.utils.CollectionUtils;
import com.diga.generic.utils.ReflexUtils;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.*;

public class KV {
    // 基本值集合类型
    private static final Set<Class<?>> BASIC_VALUE_TYPE_SET = Sets.newHashSet(
            int.class, Integer.class,
            double.class, Double.class,
            float.class, Float.class,
            long.class, Long.class,
            short.class, Short.class,
            boolean.class, Boolean.class,
            byte.class, Byte.class,
            Date.class, String.class,
            BigDecimal.class, Blob.class, Number.class);


    private Map<String, String> map;
    private Map<Class, Object> buildOneTypeCache = new HashMap<>();
    private Map<Class, Collection> buildListTypeCache = new HashMap<>();


    public KV(Map<String, String> map) {
        this.map = map;
    }

    /*==================================================*/
    /*===========      带默认值的 GET 方法       =========*/
    /*==================================================*/

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

    /*==================================================*/
    /*===========      不带默认值的 GET方法      =========*/
    /*==================================================*/

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


    /**
     * 从 key 中获取指定类型的值, 需要强调的是, 除了支持 基本类型 外,
     * 我们这里还支持 BigDecimal 类型, String 类型, Date 类型...
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        Object val = null;
        if (clazz == int.class || Integer.class == clazz) {
            val = getInteger(key);
        } else if (clazz == double.class || Double.class == clazz) {
            val = getDouble(key);
        } else if (clazz == float.class || Float.class == clazz) {
            val = getFloat(key);
        } else if (clazz == long.class || Long.class == clazz) {
            val = getLong(key);
        } else if (clazz == short.class || Short.class == clazz) {
            val = getShort(key);
        } else if (clazz == boolean.class || Boolean.class == clazz) {
            val = getBoolean(key);
        } else if (clazz == Date.class) {
            val = new Date(getLong(key));
        } else if (clazz == byte.class || Byte.class == clazz) {
            val = getByte(key);
        } else if (clazz == String.class) {
            val = getString(key);
        } else if (BigDecimal.class.isAssignableFrom(clazz)) {
            val = new BigDecimal(getString(key));
        } else if (Number.class.isAssignableFrom(clazz)) {
            val = getDouble(key);
        }
        return (T) val;
    }

    /**
     * 将 properties 中的属性转换为对应的实体类, 转换的实体类必须存在无参构造器
     *
     * @param prefix properties 的前缀, 如果没有前缀. 则传入 null
     * @param clazz  实体类类型
     * @param <T>
     * @return 返回转换后的实体类
     */
    public <T> T build(String prefix, Class<T> clazz) {
        // 这里, 首先做一下退出条件. 因为这个方法是递归的. 所以我们需要存在退出条件
        Object cache = buildOneTypeCache.get(clazz);
        if (cache != null) {
            return (T) cache;
        }

        T instance = null;
        try {
            instance = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                String fieldName = f.getName();
                if (prefix != null) {
                    fieldName = prefix + "." + fieldName;
                }
                Class<?> type = f.getType();

                Object value = null;
                // 如果它是我们支持的可以直接转换的类型
                if (BASIC_VALUE_TYPE_SET.contains(type)) {
                    value = get(fieldName, type);
                }
                // 这个字段是一个 对象类型
                else {
                    // 如果这个字段是一个集合类型, 那么我们这里就需要获取它的泛型
                    if (Collection.class.isAssignableFrom(type)) {
                        Type[] types = ReflexUtils.getFieldParameterizedType(f);
                        // 如果这个字段是一个，因为是基于发射来获取对象类型, 所以如果没有字段没有 标注泛型 我就不管了
                        // 或者如果故意写了个 Map 上去, 那么我也直接忽略了
                        if (types.length == 1) {
                            // value = buildCollection((Class<? extends Collection>) type, (Class) types[0]);
                        } else {
                            value = build(prefix, type);
                        }
                    }
                }

                try {
                    BeanUtils.copyProperty(instance, fieldName, value);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            buildOneTypeCache.put(clazz, instance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }


    // 将 properties 中的元素直接映射到集合中, 泛型中表示的元素必须存在无参数构造器
    public <T> Collection<T> buildCollection(String prefix, Class<? extends Collection> collectionType, Class<T> clazz) {
        Collection<T> collection = buildListTypeCache.get(clazz);
        if (collection != null) {
            return collection;
        }

        Field[] fields = clazz.getDeclaredFields();
        Map<String, T> listMap = new HashMap();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey();

            if (!k.startsWith(prefix)) {
                continue;
            }

            for (Field f : fields) {
                // spring.datasource.username.datasource1='root1'
                // spring.datasource.password.datasource1='123456'
                // spring.datasource.username.datasource2='root2'
                // spring.datasource.password.datasource2='123456'

                // List<DataSource> dataSources = [DataSource('root1', '123456'), DataSource('root2', '123456')];

                // 拿到 spring.datasource.username. 这个不完整字段
                String fPrefix = prefix + "." + f.getName() + ".";
                // 判断 map 中的 key 的名称是否是以这个 spring.datasource.username. 不完整字段开头的
                if (k.startsWith(fPrefix)) {
                    // 拿到后缀, 也就是 datasource1, datasource2 这些...
                    String remain = k.substring(fPrefix.length());
                    // 如果后缀里面已经没有 . 了, 表示它的确是一个终止字段, 如果像 spring.datasource.username.db1.entry=1 这样就不行
                    if (!remain.contains(".")) {
                        // 从 map 中获取已经实例化的相同后缀名的实例, 这里我们的 listMap 设计是这样的
                        // {k: "datasource1", v: DataSource('root1', '123456')}, {k: "datasource2", v: DataSource('root2', '123456')}
                        T instance = listMap.get(remain);

                        // 实例还不存在
                        if (instance == null) {
                            instance = ReflexUtils.tryInstance(clazz);
                            if (instance == null) {
                                throw new IllegalArgumentException("无法实例化类" + clazz.getName());
                            }
                            // 加入缓存
                            listMap.put(remain, instance);
                        }

                        try {
                            Object val = get(k, f.getType());
                            BeanUtils.copyProperty(instance, f.getName(), val);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        // 将 map 中的值转换为一个集合
        Collection val = CollectionUtils.to(listMap.values(), collectionType);

        // 这里是使用了一个类似 es6 的集合遍历方式
        CollectionUtils.forEach(val, (index, data, list) -> {
            // 填充特殊字段, 比如 List, 类字段
            Field[] fieldList = data.getClass().getDeclaredFields();
            for (Field field : fieldList) {
                Class<?> fType = field.getType();
                Object value = null;

                // 处理集合中元素的定义的集合属性
                if (Collection.class.isAssignableFrom(fType)) {
                    Type[] types = ReflexUtils.getFieldParameterizedType(field);
                    if (types.length == 1) {
                        // 开始套娃
                        value = buildCollection(prefix, (Class<? extends Collection>) fType, (Class) types[0]);
                    }
                } else if (!BASIC_VALUE_TYPE_SET.contains(fType)) {
                    value = build(prefix, fType);
                } else {
                    continue;
                }
                try {
                    BeanUtils.copyProperty(data, field.getName(), value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        // 加入缓存中, 防止无限套娃
        buildListTypeCache.put(clazz, val);
        return val;
    }
}
