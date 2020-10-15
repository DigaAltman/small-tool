package com.diga.db.result;

import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import com.diga.db.core.factory.ResultMapFactory;
import com.diga.generic.utils.ClassUtils;
import com.diga.generic.utils.ReflexUtils;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class DefaultResultRowHandler implements ResultRowHandler {

    private ResultMapFactory resultMapFactory;

    public DefaultResultRowHandler(ResultMapFactory resultMapFactory) {
        this.resultMapFactory = resultMapFactory;
    }

    /**
     * 基于 resultMap 进行返回结果处理
     *
     * @param map
     * @param resultMap
     * @param <T>
     * @return
     */
    @Override
    public <T extends Serializable> T handleOne(LinkedHashMap map, ResultMap resultMap) {

        // 1. 获取当前需要依赖的类
        Set<ResultMap> dependencies = resultMap.dependencies();

        // 2. 进行类的简单值填充
        Map<ResultMap, Object> beforeClassMap = new LinkedHashMap();
        dependencies.forEach(result -> beforeClassMap.put(result, result.mapToBean(map)));

        // 4. 进行真正的依赖填充
        T bean = (T) resultMap.fillBean(beforeClassMap);

        return bean;
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
    public <T> T handleOne(LinkedHashMap map, Class<T> returnClass) {
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
        return handleOne(map, resultMapFactory.build((Class<Serializable>) returnClass));
    }


    /**
     * 基于 returnClass 进行一对多映射处理
     *
     * @param result
     * @param returnClass
     * @return
     */
    @Override
    public <T> List<T> handleList(List<T> result, Class<T> returnClass) {
        if (Map.class.isAssignableFrom(returnClass)) {
            return result;
        }
        if (Set.class.isAssignableFrom(returnClass)) {
            return result;
        }
        if (Object[].class.isAssignableFrom(returnClass)) {
            return result;
        }
        if (List.class.isAssignableFrom(returnClass)) {
            return result;
        }
        if (ClassUtils.isValueType(returnClass)) {
            return result;
        }

        ResultMap resultMap = resultMapFactory.build((Class<? extends Serializable>) returnClass);
        result = handleList(result, resultMap);
        return result;
    }

    /**
     * 基于 resultMap 进行一对多映射处理
     *
     * @param result
     * @param resultMap
     * @return
     */
    @Override
    public <T> List<T> handleList(List<T> result, ResultMap resultMap) {
        return doHandleList(result, resultMap, new LinkedList<ResultMap>());
    }


    /**
     * 核心方法, 处于一对多映射
     *
     * @param list               需要处理的数据
     * @param resultMap          结果集映射
     * @param alreadyReadMapList 已经处理过的结果集映射列表, 避免无限递归
     * @param <T>
     * @return
     */
    private <T> List<T> doHandleList(List<T> list, ResultMap resultMap, List<ResultMap> alreadyReadMapList) {

        // 递归调用终止条件
        if (alreadyReadMapList.contains(resultMap)) {
            return list;
        }

        alreadyReadMapList.add(resultMap);

        List res = null;

        List<Result> childResult = resultMap.childMapResult();
        for (Result el : childResult) {
            boolean one = false;
            ResultMap map = el.getCollection();

            // 判断对应关系, resultMap 和 map 之间的对应关系是 一对一 还是 一对多
            if (map == null) {
                one = true;
                map = el.getAssociation();
            }

            List childList = new ArrayList();

            // 循环处理每一行数据
            for (Object t : list) {
                Object child = ReflexUtils.get(t, el.getProperty());

                // 如果是 1:N, 那么我们返回的处理完一对一映射后的数据应该是 Course = { 1, '语文', List[Student = {1, '小白'}] }
                if (!one) {
                    // 从每行数据的 List 中, 取出 Student 这个数据 Student = {1, '小白'}
                    List objectList = (List) child;
                    if (objectList.size() > 0) {
                        childList.add(objectList.get(0));
                    }
                    // 然后封装成一个 List[Student= {..}, Student = {..}, Student = {..}, ..]
                } else {

                    childList.add(child);
                }

            }

            res = doHandleList(childList, map, alreadyReadMapList);


            // 如果是一对一, 那么我们就将 Course 和 Student 的值合并在一起,作为一个key,
            if (one) {
                List<String> fieldList = map.id();

                // 这个 objectMap 用来存放 一对一 映射中的子方的.
                Map<String, Object> objectMap = new HashMap();

                // 这一步的打算就是计算出每个 Course 的值, 以及对应的 Course 对象
                for (Object child : res) {
                    if (child != null) {
                        String key = values(child, fieldList);
                        if (!objectMap.containsKey(key)) {
                            objectMap.put(key, child);
                        }
                    }

                }


                // 让 Student 中的 Course 字段 指向 同一个 Object 引用
                for (Object teacher : list) {
                    Object course = ReflexUtils.get(teacher, el.getProperty());

                    if (course != null) {
                        String key = values(course, fieldList);
                        ReflexUtils.set(teacher, el.getProperty(), objectMap.get(key));
                    }
                }

                // 此时, Student 下的 Course 引用已经解决了. 现在要解决的就是不同的 Course
                Set<String> teacherSet = new HashSet();

                // 保证顺序性
                List<Object> resultList = new ArrayList();

                List<String> fList = resultMap.id();
                for (Object teacher : list) {
                    String key = values(teacher, fList);
                    if (!teacherSet.contains(key)) {
                        teacherSet.add(key);
                        resultList.add(teacher);
                    }
                }


                // 最后一步, 将 Student 的值进行一个整合, 然后返回
                return resultList.stream().map(bean -> (T) bean).collect(Collectors.toList());
            }

            // 如果是一对多, 我们要做的事情就是首先还是要过滤一遍 Teacher,
            else {
                if (res.size() > 0) {
                    // 定义一个存放一方数据的Map, key就是这个一方的数据的唯一表示
                    List<Object> oneList = new ArrayList();
                    Set<String> oneKeySet = new HashSet();

                    // 定义一个存放多方数据的Map, key就是这个一方数据的唯一表示
                    Map<String, List> tMap = new HashMap();

                    // 获取当前resultMap中的id字段,如果没用定义id,则取不涉及映射的所有字段组成作为一个id大字段
                    List<String> fList = resultMap.id();

                    // 循环遍历一方的数据,因为多方的数据其实就是一方的数据的子数据的合集, 所以这里一方的数据和多方的数据长度
                    // 是一致的, 以至于我们可以通过同样的 i 索引来获取
                    for (int i = 0; i < list.size(); i++) {
                        Object teacher = list.get(i);

                        List childElements = ReflexUtils.get(teacher, el.getProperty());
                        Object course = null;
                        if (childElements.size() > 0) {
                            course = res.get(i);
                        }

                        // 基于一方的数据和一方的id字段得到一个key, 这个key的内容如下: "id=1&name=张三&"
                        String key = values(teacher, fList);

                        // 如果在这个一方数据的Map中,不存在同样的key了, 我们在添加进去
                        if (!oneKeySet.contains(key)) {
                            oneKeySet.add(key);
                            oneList.add(teacher);
                        }

                        // 从存放多方数据的Map中取出这个key对应的List
                        List courseList = tMap.get(key);

                        // 如果不存在,则创建并添加进去
                        if (courseList == null) {
                            courseList = Lists.newArrayList();
                            tMap.put(key, courseList);
                        }

                        // 这个list再添加多方的数据
                        if (course != null) {
                            courseList.add(course);
                        }
                    }

                    // 将 tMap 和 teacherMap 通过相同的 key 进行合并
                    for (Object o : oneList) {
                        String key = values(o, fList);
                        List courseList = tMap.get(key);

                        // 修改一方中对应的多方的引用
                        ReflexUtils.set(o, el.getProperty(), courseList);

                    }

                    return oneList.stream().map(bean -> (T) bean).collect(Collectors.toList());
                }
            }
        }

        return list;
    }


    /**
     * 根据每个bean中对应的字段得到一个唯一的值
     *
     * @param bean      实例对象
     * @param fieldList 字段集合
     * @return
     */
    private String values(Object bean, List<String> fieldList) {
        StringBuilder sb = new StringBuilder();
        for (String field : fieldList) {
            try {
                Field f = bean.getClass().getDeclaredField(field);
                f.setAccessible(true);
                sb.append(f.getName()).append("=");
                Object v = f.get(bean);
                if (v == null) {
                    sb.append("null");
                } else {
                    sb.append(v.toString());
                }
                sb.append("&");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
