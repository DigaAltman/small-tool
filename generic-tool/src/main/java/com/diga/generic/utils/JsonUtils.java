package com.diga.generic.utils;

import com.google.gson.Gson;

public class JsonUtils {
    private static final Gson gson = new Gson();

    /**
     * 和 JS 中的JSON.parse一样
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }


    /**
     * 和 JS 中的 JSON.stringify 一样
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> String stringify(T bean) {
        return gson.toJson(bean, bean.getClass());
    }
}
