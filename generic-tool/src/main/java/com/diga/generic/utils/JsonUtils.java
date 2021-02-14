package com.diga.generic.utils;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.util.*;

public class JsonUtils {
    private static final Gson GSON = new Gson();
    private static final JsonParser PARSER = new JsonParser();

    // 将字符串转换为 json 化后的 Object 对象
    private static JsonObject jsonObject(String json) {
        try {
            return PARSER.parse(json).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            log.error("无法解析 json 字符串", e);
        }
        return null;
    }

    // 将字符串转换为 json 化后的 Array 对象
    private static JsonArray jsonArray(String json) {
        return PARSER.parse(json).getAsJsonArray();
    }

    // 将 json 转换为 List
    public static <T> List<T> toList(String json, Class<T> clazz) {
        return Lists.newArrayList((T) GSON.fromJson(json, TypeToken.getArray(clazz).getType()));
    }

    // 将一个 json 字符串转换为 Set, 并且指定 Set 元素中的 Bean 类型
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        return new HashSet<>(toList(json, clazz));
    }

    // 底层的将一个 array 化的 json 对象转换为 List 对象的方法
    private static List<Object> toList(JsonArray jsonArray) {
        List<Object> list = new ArrayList();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object val = jsonArray.get(i);
            if (val instanceof JsonArray) {
                list.add(toList((JsonArray) val));
            } else if (val instanceof JsonObject) {
                list.add(toMap((JsonObject) val));
            } else {
                list.add(val);
            }
        }
        return list;
    }


    public static <K, V> Map<K, V> toMap(JsonObject jsonObject, Class<K> keyClass, Class<V> valClass) {
        Map<K, V> map = new HashMap();
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            K k = parse(key, keyClass);

            Object value = entry.getValue();
            V v = parse(stringify(value), valClass);
            map.put(k, v);
        }
        return map;
    }


    // 将一个 json 对象转换为 Map[String,Object] 对象的方法
    public static Map<String, Object> toMap(String json) {
        return toMap(Objects.requireNonNull(jsonObject(json)));
    }

    // 底层的将一个 json 对象转换为 map 对象的方法
    private static Map<String, Object> toMap(JsonObject jsonObject) {
        Map<String, Object> map = new HashMap();
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray) {
                map.put(key, toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                map.put(key, toMap((JsonObject) value));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }


    public static List<Object> toList(String json) {
        return toList(jsonArray(json));
    }

    /**
     * 和 JS 中的JSON.parse一样
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }


    /**
     * 和 JS 中的 JSON.stringify 一样
     *
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> String stringify(T bean) {
        return GSON.toJson(bean, bean.getClass());
    }

    public static Set toSet(String json) {
        return new HashSet(toList(json));
    }

    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        return toMap(Objects.requireNonNull(jsonObject(json)), kClass, vClass);
    }

}
