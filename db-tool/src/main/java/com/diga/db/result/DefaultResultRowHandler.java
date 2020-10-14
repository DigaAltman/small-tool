package com.diga.db.result;

import com.diga.db.core.ResultMap;
import com.diga.db.core.factory.ResultMapFactory;
import com.diga.generic.utils.ClassUtils;
import com.diga.generic.utils.ReflexUtils;

import java.io.Serializable;
import java.util.*;

public class DefaultResultRowHandler implements ResultRowHandler {

    private ResultMapFactory resultMapFactory;

    /**
     * 基于 resultMap 进行返回结果处理
     *
     * @param map
     * @param resultMap
     * @param <T>
     * @return
     */
    @Override
    public <T extends Serializable> T handle(LinkedHashMap map, ResultMap resultMap) {

        return null;
    }

    /**
     * 基于 returnClass 进行返回结果处理
     *
     * @param map
     * @param returnClass
     * @param <T>
     * @return
     */
    @Override
    public <T extends Serializable> T handle(LinkedHashMap map, Class<T> returnClass) {
        // 处理 Map 返回值类型
        if (Map.class.isAssignableFrom(returnClass)) {
            return (T) map;
        }

        // 处理 Set 返回值类型
        if (Set.class.isAssignableFrom(returnClass)) {
            Set set = new LinkedHashSet();
            map.forEach((k, v) -> set.add(v));
            return (T) set;
        }

        // 处理 Object[] 返回值类型
        if (Object[].class.isAssignableFrom(returnClass)) {
            Collection values = map.values();
            return (T) values.toArray(new Object[values.size()]);
        }

        // 处理 List 返回值类型
        if (List.class.isAssignableFrom(returnClass)) {
            List list = new LinkedList();
            map.forEach((k, v) -> list.add(v));
            return (T) list;
        }

        // 如果是基本类型,比如 Integer, int, float ... 之类的, 直接返回就好了
        if (ClassUtils.isValueType(returnClass)) {
            return ReflexUtils.conversionValue(map.values().toArray()[0].toString(), returnClass);
        }

        // 其他返回值类型
        return handle(map, resultMapFactory.build(returnClass));
    }
}
