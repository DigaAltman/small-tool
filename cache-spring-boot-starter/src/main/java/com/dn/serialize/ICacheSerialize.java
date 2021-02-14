package com.dn.serialize;

import java.util.List;

public interface ICacheSerialize {

    /**
     * 将 bean 序列化成 json 字符串
     *
     * @param bean
     * @return
     */
    String serialize(Object bean);


    /**
     * 将字符串序列化为 JavaBean
     *
     * @param json
     * @param methodReturnType
     * @param genericTypes
     * @return
     */
    Object deserialization(String json, Class methodReturnType, List<Class> genericTypes);

}
