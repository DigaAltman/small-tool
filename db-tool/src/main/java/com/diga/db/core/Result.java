package com.diga.db.core;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 处理结果映射中的每个字段和 SQL 返回的字段的映射关系
 *
 * 它可以是这样的:
 *
 * <code>
 *     <id prototype="appId" column="app_id"/>
 * </code>
 *
 * 或者是这样的:
 * <code>
 *     <result prototype="appName" column="app_name"/>
 * </code>
 *
 * 或者是这样的:
 * <code>
 *     <association prototype="course" ref="courseMap"/>
 * </code>
 *
 * <code>
 *     <collection prototype="studentList" ref="studentMap"/>
 * </code>
 *
 * ....
 */
@Data
@Accessors(chain = true)
public class Result {
    /**
     * SQL 返回结果的字段名称
     */
    protected String column;

    /**
     * 对应的实体类的属性
     */
    protected String property;

    /**
     * 主键标识
     */
    protected boolean primary;

    /**
     * 字段对应的 JAVA 类型
     */
    protected Class<?> type;

    /**
     * 一对一 关联映射
     */
    protected ResultMap association;

    /**
     * 一对多 关联映射
     */
    protected ResultMap collection;
}
