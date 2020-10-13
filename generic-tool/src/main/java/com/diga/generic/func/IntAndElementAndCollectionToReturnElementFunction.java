package com.diga.generic.func;

import java.util.Collection;

/**
 * @param <Param>  传入类型
 * @param <Return> 返回类型
 */
public interface IntAndElementAndCollectionToReturnElementFunction<Param, Return> {

    Return doFunc(int index, Param data, Collection<Param> dataCollection);
}
