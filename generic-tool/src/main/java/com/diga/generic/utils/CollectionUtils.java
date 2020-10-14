package com.diga.generic.utils;

import com.diga.generic.func.IntAndElementAndCollectionToReturnElementFunction;
import com.diga.generic.func.IntAndElementAndCollectionToReturnVoidFunction;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 * 针对集合操作的工具类
 */
public class CollectionUtils {

    /**
     * 按照 NodeJs 的 forEach 进行制作
     *
     * @param collections 处理集合
     * @param function    处理函数
     * @param <T>         处理集合中泛型
     */
    public static <T> void forEach(Collection<T> collections, IntAndElementAndCollectionToReturnVoidFunction<T> function) {
        int index = 0;
        Iterator<T> iterator = collections.iterator();
        while (iterator.hasNext()) {
            function.doFunc(index, iterator.next(), collections);
            index++;
        }
    }


    /**
     * 按照 NodeJs 的 map 进行制作
     *
     * @param collections 集合元素
     * @param function    处理函数
     * @param <T>         处理集合中的泛型
     * @param <K>         返回集合中的泛型
     * @return
     */
    public static <T, K> Collection<K> map(Collection<T> collections, IntAndElementAndCollectionToReturnElementFunction<T, K> function) {
        int index = 0;
        Iterator<T> iterator = collections.iterator();

        Collection<K> dataList = new ArrayList();

        while (iterator.hasNext()) {
            K returnValue = function.doFunc(index, iterator.next(), collections);
            dataList.add(returnValue);
            index++;
        }

        return dataList;
    }


    /**
     * 按照 NodeJs 的 filter 进行制作
     *
     * @param collections 集合元素
     * @param function    处理函数
     * @param <T>         处理集合中的泛型
     * @return
     */
    public static <T> Collection filter(Collection<T> collections, IntAndElementAndCollectionToReturnElementFunction<T, Boolean> function) {
        int index = 0;
        Iterator<T> iterator = collections.iterator();

        while (iterator.hasNext()) {
            boolean status = function.doFunc(index, iterator.next(), collections);
            if (!status) {
                iterator.remove();
            }
            index++;
        }

        return collections;
    }


    /**
     * 按照 NodeJs 的 reduce 进行制作
     *
     * @param collections 集合元素
     * @param function    处理函数
     * @param <T>         处理集合中的泛型
     * @return
     */
    public static <T> T reduce(Collection<T> collections, BinaryOperator<T> function) {
        Iterator<T> iterator = collections.iterator();
        int index = 0;
        T lastReturn = null;
        while (iterator.hasNext()) {
            if (index < 1) {
                lastReturn = iterator.next();
            } else {
                lastReturn = function.apply(lastReturn, iterator.next());
            }
            index++;
        }

        return lastReturn;
    }


    /**
     * 转换 Collection 类
     *
     * @param value
     * @param type
     * @return
     */
    public static Collection to(Collection value, Class<?> type) {
        if (List.class.isAssignableFrom(type)) {
            List list = new LinkedList();
            forEach(value, (index, data, dataList) -> list.add(index, data));
            value = list;
        }

        if (Set.class.isAssignableFrom(type)) {
            Set set = new LinkedHashSet();
            forEach(value, (index, data, dataList) -> set.add(data));
            value = set;
        }

        // 以后不够自己补充
        return value;
    }
}
