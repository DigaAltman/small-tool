package com.dn.serialize.support;

import com.diga.generic.utils.JsonUtils;
import com.dn.serialize.ICacheSerialize;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultCacheSerialize implements ICacheSerialize {

    @Override
    public String serialize(Object bean) {
        return JsonUtils.stringify(bean);
    }

    @Override
    public Object deserialization(String json, Class methodReturnType, List<Class> genericTypes) {
        if (List.class.isAssignableFrom(methodReturnType)) {
            if (genericTypes != null && genericTypes.size() > 0) {
                return JsonUtils.toList(json, genericTypes.get(0));
            }
            return JsonUtils.toList(json);
        } else if (Set.class.isAssignableFrom(methodReturnType)) {
            if (genericTypes != null && genericTypes.size() > 0) {
                return JsonUtils.toSet(json, genericTypes.get(0));
            }
            return JsonUtils.toSet(json);
        } else if (Map.class.isAssignableFrom(methodReturnType)) {
            if (genericTypes != null && genericTypes.size() >= 2) {
                return JsonUtils.toMap(json, genericTypes.get(0), genericTypes.get(1));
            }
            return JsonUtils.toMap(json);
        }
        return JsonUtils.parse(json, methodReturnType);
    }
}
