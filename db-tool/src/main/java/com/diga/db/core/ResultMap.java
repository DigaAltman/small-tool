package com.diga.db.core;

import com.diga.generic.utils.ClassUtils;
import com.diga.generic.utils.ReflexUtils;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;

@Data
@NoArgsConstructor
public class ResultMap {

    /**
     * 返回结果映射容器对应的 id
     */
    private String id;

    /**
     * 返回结果映射容器对应的类全路径名称
     */
    private Class<? extends Serializable> type;

    /**
     * 字段对应的子字段
     */
    @ToString.Exclude
    private List<Result> resultList = new ArrayList();


    public ResultMap(String id, Class<? extends Serializable> type) {
        this.id = id;
        this.type = type;
    }


    /**
     * 获取当前ResultMap下的所有的ResultMap， 包括 子ResultMap 和 孙ResultMap ...
     * 包括当前ResultMap
     *
     * @return
     */
    public Set<ResultMap> dependencies() {
        Set<ResultMap> resultMapSet = new LinkedHashSet();

        getDependencies(this, resultMapSet);
        return resultMapSet;
    }

    /**
     * 具体获取 ResultMap 下的所有 ResultMap 的核心方法
     *
     * @param resultMap
     * @param resultMapSet
     */
    private void getDependencies(ResultMap resultMap, Set<ResultMap> resultMapSet) {
        // 这里不能直接使用 resultMapSet.contains(resultMap) ,因为 Set 的 contains 方法,会出现 StackException
        if (!resultMapSet.contains(resultMap)) {
            resultMapSet.add(resultMap);

            List<Result> resultList = resultMap.childMapResult();

            resultList.stream()
                    .filter(f -> f.collection != null || f.association != null)
                    .forEach(f -> {
                        ResultMap map = f.association == null ? f.collection : f.association;
                        getDependencies(map, resultMapSet);
                    });
        }
    }


    /**
     * 获取当前的 ResultMap 下的 子 Result， 包括 collection， association
     *
     * @return
     */
    public List<Result> childMapResult() {
        LinkedList<Result> resultList = Lists.newLinkedList();
        for (Result result : this.getResultList()) {
            if (result.association != null || result.collection != null) {
                resultList.add(result);
            }
        }

        return resultList;
    }

    /**
     * 简单填充
     *
     * @param map
     * @return
     */
    public Object mapToBean(Map<String, Object> map) {
        Object instance = null;
        try {
            // 获取 resultMap 的返回值类型
            instance = ReflexUtils.tryInstance(type);

            if (instance == null) {
                throw new IllegalArgumentException("类 " + type + " 无法实例化, 必须提供无参构造器");
            }

            boolean setStatus = false;

            List<Result> resultList = getResultList();

            for (int i = 0; i < resultList.size(); i++) {

                // 基于 resultMap 中的映射关系,进行循环赋值操作
                Result result = resultList.get(i);

                // 不会处理 一对一 和 一对多， 只处理简单类型
                if (result.collection == null && result.association == null) {

                    // 从数据库查询结果转换的 map 中获取对应 k 的 值, 然后基于反射进行赋值
                    Object val = map.get(result.getColumn());

                    if (val != null) {
                        ReflexUtils.set(instance, result.getProperty(), val);
                        setStatus = true;
                    }
                }
            }

            // 当没有字段进行赋值时
            if (setStatus == false) {
                return null;
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("类实例化失败,错误原因:" + e.getMessage());
        }

        return instance;
    }


    /**
     * 也是一个很重要的方法, 这个方法的作用是进行真正的依赖填充
     * 它会解决 ORM 中的 一对一 循环依赖
     *
     * @param beforeClassMap 解决一对一循环依赖前的对象, 这里所有的 Object 都是只完成了简单字段的值映射.
     *                       还没有完成引用类型字段的值映射
     * @return
     */
    public Object fillBean(Map<ResultMap, Object> beforeClassMap) {
        // 完成 ORM 的 一对一 映射后的集合
        Map<ResultMap, Object> successBeanMap = new HashMap();

        // 循环处理这些还没有解决一对一循环依赖的对象
        beforeClassMap.forEach((k, v) -> {
            // 取出这个没有完成 一对一 循环依赖字段设置的简单对象
            Object bean = v;

            // 根据它的 ResultMap 中的 List<ResultField> 来进行处理. 这里我们把 一对多 映射 也在这里做了一个简单的处理
            k.getResultList().stream().filter(f -> f.association != null || f.collection != null).forEach(f -> {

                // 拿到一对一映射
                ResultMap map = f.association;

                // 如果不是一对一,那么就是一对多映射
                if (map == null) {
                    map = f.collection;
                }
                // 尝试从 完成缓存中 拿到这个 ResultMap 对应的结果
                Object res = successBeanMap.get(map);

                // 如果拿不到,就去 简单缓存中 拿到这个 ResultMap 对应的结果
                if (res == null) {
                    res = beforeClassMap.get(map);
                }

                // 如果这个字段它是 一对多 引用的话
                if (f.collection != null) {
                    if (res != null) {
                        // 那我们就在赋值的时候,以 List 的形式扔进去
                        ReflexUtils.set(bean, f.getProperty(), Arrays.asList(res));
                    } else {
                        // 如果没有查询出多方有内存, 那就赋值一个空 List
                        ReflexUtils.set(bean, f.getProperty(), Arrays.asList());
                    }
                } else {
                    if (bean != null) {
                        // 如果是一对一,那就直接扔进去
                        ReflexUtils.set(bean, f.getProperty(), res);
                    }
                }

            });

            // 最后将这个完成映射处理后的 bean 扔到 successBeanMap 缓存中.
            // 循环套娃解决的思路就是手动更改指针引用
            successBeanMap.put(k, bean);
        });

        // 这里我们就可以清空这个简单缓存了
        beforeClassMap.clear();

        return successBeanMap.get(this);
    }


    /**
     * 获取当前 ResultMap 下的所有 Result 字段, 获取它的 id 标签, 如果没有配置 id 标签, 则将所有的
     * result 标签组合成一个 临时id 标签
     *
     * @return
     */
    public List<String> id() {
        for (Result result : resultList) {
            if (result.primary) {
                return Lists.newArrayList(result.property);
            }
        }

        List<String> res = Lists.newArrayList();
        for (Result result : resultList) {
            if (result.collection == null && result.association == null) {
                res.add(result.property);
            }
        }

        return res;
    }


    /*===========================================================*/
    /*================ 重写 equals 和 hashCode 方法 ================*/
    /*===========================================================*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultMap)) return false;
        ResultMap resultMap = (ResultMap) o;
        return Objects.equals(getId(), resultMap.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
