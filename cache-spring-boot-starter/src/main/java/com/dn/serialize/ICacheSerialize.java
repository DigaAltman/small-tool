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
     * @param json             缓冲中记录的字符串
     * @param methodReturnType 方法的返回值类型
     * @param genericTypes     方法返回值类型中标注的泛型
     * @return
     */
    Object deserialization(String json, Class methodReturnType, List<Class> genericTypes);

}
