package com.diga.generic.func;

import java.util.Collection;

@FunctionalInterface
public interface IntAndElementAndCollectionToReturnVoidFunction<T> {


    void doFunc(int index, T currentData, Collection<T> dataCollection);

}
