package com.diga.orm.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * 字段详情
 */
@Data
public class ColumnDetail implements Serializable {

    // 字段名称
    private String column;

    // 对应的 java 属性名称
    private String property;

    // 字段类型
    private String sqlType;

    // 对应的 java 类型
    private Class javaType;

    // 是否允许为 null
    private boolean allowNull;

    // 索引类型, PRI, UNI, MUL
    private String Key;

    // 索引名称
    private String keyName;

    // 字段在索引中的顺序
    private Long seqInIndex;

    // 字段默认值
    private String defaultValue;

    // 索引是否有顺序, A(升序), NULL(无分类)
    private String collation;

    // 索引类型 [FULLTEXT, HASH, BTREE, RTREE]
    private String indexType;

    // 字段备注
    private String comment;

    // 索引备注
    private String indexComment;

    // 字段附加
    private String extra;

}
