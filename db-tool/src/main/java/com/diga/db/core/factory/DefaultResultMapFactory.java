package com.diga.db.core.factory;

import com.diga.db.annotation.Column;
import com.diga.db.annotation.Id;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import com.diga.generic.utils.ReflexUtils;
import com.diga.generic.utils.StringUtils;
import com.google.common.collect.Sets;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 管理 ResultMap 对象的工厂, 所有的 ResultMap 都会通过这个ResultMap 进行
 */
public class DefaultResultMapFactory implements ResultMapFactory {

    /**
     * ORM 映射的基本类型
     */
    public static Set<Class> basicTypeSet = Sets.newHashSet(String.class, Byte.class, Character.class, Boolean.class, Integer.class, Double.class, Float.class, Short.class, Long.class, byte.class, char.class, boolean.class, int.class, double.class, float.class, short.class, long.class, BigDecimal.class, Date.class, Blob.class);

    /**
     * ResultMap 的缓存容器, 缓存彻底初始化的 ResultMap, 用户自定义的 ResultMap 解析后的结果也应该要放在这里
     */
    private final Map<String, ResultMap> DEFAULT_RESULT_MAP = new HashMap<>(64);

    /**
     * ResultMap 的缓存容器, 缓存已经简单初始化的 ResultMap， 里面关于引用其他 ResultMap 的字段引用一直是 null
     */
    private final Map<String, ResultMap> EASY_RESULT_MAP = new HashMap<>(64);


    private final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();


    /**
     * 通过 resultMap 的 id 来获取存放在 DEFAULT_RESULT_MAP 中的 resultMap
     *
     * @param resultMapId
     * @return
     */
    @Override
    public ResultMap getResultMap(String resultMapId) {
        READ_WRITE_LOCK.readLock().lock();
        try {
            return DEFAULT_RESULT_MAP.get(resultMapId);
        } finally {
            READ_WRITE_LOCK.readLock().unlock();
        }
    }


    /**
     * 存放 resultMap 到 DEFAULT_RESULT_MAP 中
     *
     * @param resultMapId
     * @param resultMap
     */
    public void putResultMap(String resultMapId, ResultMap resultMap) {
        READ_WRITE_LOCK.writeLock().lock();
        try {
            DEFAULT_RESULT_MAP.put(resultMapId, resultMap);
        } finally {
            READ_WRITE_LOCK.writeLock().unlock();
        }
    }

    /**
     * 通过 resultMap 的 id 来获取存放在 EASY_RESULT_MAP 中的 resultMap
     *
     * @param resultMapId
     * @return
     */
    public ResultMap getEasyMap(String resultMapId) {
        READ_WRITE_LOCK.readLock().lock();
        try {
            return EASY_RESULT_MAP.get(resultMapId);
        } finally {
            READ_WRITE_LOCK.readLock().unlock();
        }
    }


    /**
     * 存放 resultMap 到 EASY_RESULT_MAP 中
     *
     * @param resultMapId
     * @param resultMap
     */
    public void putEasyMap(String resultMapId, ResultMap resultMap) {
        READ_WRITE_LOCK.writeLock().lock();
        try {
            EASY_RESULT_MAP.put(resultMapId, resultMap);
        } finally {
            READ_WRITE_LOCK.writeLock().unlock();
        }
    }


    public void removeEasyMap(String resultMapId) {
        READ_WRITE_LOCK.writeLock().lock();
        try {
            EASY_RESULT_MAP.remove(resultMapId);
        } finally {
            READ_WRITE_LOCK.writeLock().unlock();
        }
    }


    /**
     * 基于实体类生成 ResultMap
     *
     * @param clazz
     * @return
     */
    @Override
    public ResultMap build(Class<? extends Serializable> clazz) {
        // 判断 clazz 是否合法
        if (clazz == null || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException("无法识别的实体类, 请确保类名称为" + clazz.getName() + "的类是实体类");
        }

        // 默认 id 为 类的全路径名称
        String defaultResultMapId = clazz.getName();

        // 从彻底完成的缓存中取出对象
        ResultMap resultMap = getResultMap(defaultResultMapId);

        // 如果不存在, 才去对类进行解析
        if (resultMap == null) {
            resultMap = doBuild(clazz);
        }

        return resultMap;
    }

    /**
     * 尝试解析依赖的类,因为这个 ResultMap 中可能包含其他的 ResultMap, 其他的ResultMap中包含当前正在
     * 解析的 ResultMap， 我们需要解决这种循环引用
     *
     * @param clazz
     * @return
     */
    private ResultMap doBuild(Class<? extends Serializable> clazz) {
        // 默认 id 为 类的缩写(首字母小写) + Map, 比如: com.example.pojo.User -> userMap
        String defaultResultMapId = clazz.getName();

        // 首先从简单初始化后的 ResultMap 缓存中取出对应 id 的 ResultMap
        ResultMap resultMap = getEasyMap(defaultResultMapId);

        // 如果缓存中存在则直接返回
        if (resultMap != null) {
            return resultMap;
        }

        // 创建 ResultMap, 此时 ResultMap 中的 List<Result> 还没有数据
        resultMap = new ResultMap(defaultResultMapId, clazz.getName());

        // 开始处理 ResultMap 下的 字段映射
        List<Result> resultList = resultMap.getResultList();

        // 需要处理的所有字段
        Field[] fieldList = clazz.getDeclaredFields();

        // 需要处理的特殊字段, 比如 Course course 引发的一对一 , List<Student> studentList 引发的一对多
        List<Field> needSpecialHandleFieldList = new LinkedList();

        // 基本类型字段处理, 这里只是会找出特殊字段
        for (int i = 0; i < fieldList.length; i++) {
            // 获取当前字段
            Field field = fieldList[i];

            // 当前字段的类型
            Class columnClassType = field.getType();

            // 当前字段名称
            String prototype = field.getName();

            // 转驼峰后的 sql 字段名称
            String column = StringUtils.reverseHump(prototype);

            // 是否为主键
            boolean primaryKey = false;

            // 获取字段上的列注解, 如果用户配置了 @Column 注解,则 数据表字段名称 取 @Column 注解中的 value 值
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                column = col.value();
            }

            // 获取字段上的 @Id 注解, 如果用户配置了 @Id 注解,则表示这个字段是 ResultMap 中的 id 字段
            Id primary = field.getAnnotation(Id.class);

            if (primary != null) {
                // 因为存在 @Id("course_id") 的情况, 如果 @Id 中配置了列名称,我们就不使用 @Column 中的名称了
                column = primary.value().trim().equals("") ? column : primary.value();
                primaryKey = true;
            }

            // 构建一个 Result 然后填充进去就可以了
            Result result = new Result();
            result.setColumn(column).setType(columnClassType).setPrimary(primaryKey).setProperty(prototype);


            // 如果这个字段不属于基本类型, 那就放到 特殊类型字段集合中, 最后统一进行处理
            if (!basicTypeSet.parallelStream().anyMatch(t -> t.isAssignableFrom(columnClassType))) {
                needSpecialHandleFieldList.add(field);
            }

            resultList.add(result);
        }

        /*
         * 到了这一步, 简单类型的字段映射解析就完成了, 我们就可以将这个 ResultMap 放入到简单缓存中了
         * 下次, 在进行循环引用触发的递归调用事件时, 就可以依靠这个 easyResultMapCache 结束递归了
         */
        putEasyMap(defaultResultMapId, resultMap);

        // 特殊类型字段处理
        for (int i = 0; i < needSpecialHandleFieldList.size(); i++) {
            Field field = needSpecialHandleFieldList.get(i);
            Class columnClassType = field.getType();
            ResultMap associationMap = null;
            ResultMap collectionMap = null;

            // 如果它是 集合 类型,那么我们就需要获取它的泛型
            if (Collection.class.isAssignableFrom(columnClassType)) {
                Type[] types = ReflexUtils.getFieldParameterizedType(field);

                // 如果没有声明泛型， 则作为 Map 处理
                if (null == types) {
                    return getEasyMap("java.util.Map");
                }

                // 然后根据这个 类型 去获取对应的 ResultMap， 它此时就会去调用 build
                collectionMap = build((Class<? extends Serializable>) types[0]);

            } else {
                // 如果是一对一, 那就直接根据字段类型去获取对应的 ResultMap 就可以了
                associationMap = build(columnClassType);
            }

            // 找到对应的 Result 然后填充依赖进去就可以了
            Result result = resultMap.getResultList().stream().filter(f -> f.getProperty().equals(field.getName())).findFirst().get();

            // 这里我直接两个一起设置也不会有事, 因为这里始终有一个为 null
            result.setAssociation(associationMap).setCollection(collectionMap);
        }

        // 此时, 整个 ResultMap 已经填充完毕了. 我们在将它放入到 彻底完成的缓存中就可以了
        putResultMap(defaultResultMapId, resultMap);

        // 删除临时缓存中的 key 为 clazz 的 ResultMap
        removeEasyMap(defaultResultMapId);

        // 最终返回出去
        return resultMap;
    }

}
