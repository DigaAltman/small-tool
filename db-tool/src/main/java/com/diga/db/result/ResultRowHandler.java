package com.diga.db.result;


import com.diga.db.core.ResultMap;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * 这个接口会将数据库返回的每一行的结果都以 LinkedHashMap 的形式扔进来进行处理
 *
 * @author tomcatbbzzzs
 * @description 这个接口是用来解决 java.sql.LinkedHashMap 和 JavaBean 之间的具体转换的
 */
public interface ResultRowHandler {


    /**
     * 基于 resultMap 进行返回结果处理
     *
     * @param map
     * @param resultMap
     * @param <T>
     * @return
     */
    <T extends Serializable> T handle(LinkedHashMap map, ResultMap resultMap);


    /**
     * 基于 returnClass 进行返回结果处理
     *
     * @param map
     * @param returnClass
     * @param <T>
     * @return
     */
    <T extends Serializable> T handle(LinkedHashMap map, Class<T> returnClass);
}
