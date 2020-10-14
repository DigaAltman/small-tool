package com.diga.db.core.factory;

import com.diga.db.core.ResultMap;

import java.io.Serializable;

/**
 * 管理 ResultMap 对象的工厂, 所有的 ResultMap 都会通过这个ResultMap 进行
 */
public interface ResultMapFactory {

    /**
     * 通过 resultMap 的 id 来获取存放在 DEFAULT_RESULT_MAP 中的 resultMap
     *
     * @param resultMapId resultMap 的 id
     * @return 从缓存中获取, 不存在返回null
     */
    ResultMap getResultMap(String resultMapId);


    /**
     * 基于实体类生成 ResultMap
     *
     * @param clazz
     * @return
     */
    ResultMap build(Class<? extends Serializable> clazz);
}
