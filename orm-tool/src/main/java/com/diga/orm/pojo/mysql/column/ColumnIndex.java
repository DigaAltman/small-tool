package com.diga.orm.pojo.mysql.column;

import com.diga.db.annotation.ResultBean;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表对应的索引信息
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColumnIndex implements Serializable {
    /**
     * 表名称
     */
    private String table;

    /**
     * 如果索引不能包括重复词，则为0。如果可以，则为1.
     */
    private Long nonUnique;

    /**
     * 索引的名称
     */
    private String keyName;

    /**
     * 索引中的列序列号，从1开始
     */
    private Long seqInIndex;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列以什么方式存储在索引中。在MySQL中，有值‘A’（升序）或NULL（无分类）
     */
    private String collation;

    /**
     * 索引中唯一值的数目的估计值。通过运行ANALYZE TABLE或myisamchk -a可以更新。基数根据被存储为整数的统计数据来计数，
     * 所以即使对于小型表，该值也没有必要是精确的。基数越大，当进行联合时，MySQL使用该索引的机会就越大。
     */
    private Long cardinality;

    /**
     * 如果列只是被部分地编入索引，则为被编入索引的字符的数目。如果整列被编入索引，则为NULL
     */
    private String subPart;

    /**
     * 指示关键字如何被压缩。如果没有被压缩，则为NULL
     */
    private String packed;

    /**
     * 如果列含有NULL，则含有YES。如果没有，则该列含有NO
     */
    private String nullValue;

    /**
     * 用过的索引方法（BTREE, FULLTEXT, HASH, RTREE).
     */
    private String indexType;

    /**
     * 字段备注
     */
    private String comment;

    /**
     * 索引备注
     */
    private String indexComment;


    @ResultBean(id = "com.diga.orm.pojo.mysql.column.ColumnIndex")
    public ResultMap tableIndex() {
        ResultMap resultMap = new ResultMap("", ColumnIndex.class);
        List<Result> resultList = resultMap.getResultList();
        resultList.add(new Result().setColumn("Table").setProperty("table"));
        resultList.add(new Result().setColumn("Non_unique").setProperty("nonUnique"));
        resultList.add(new Result().setColumn("Key_name").setProperty("keyName"));
        resultList.add(new Result().setColumn("Seq_in_index").setProperty("seqInIndex"));
        resultList.add(new Result().setColumn("Column_name").setProperty("columnName"));
        resultList.add(new Result().setColumn("Collation").setProperty("collation"));
        resultList.add(new Result().setColumn("Cardinality").setProperty("cardinality"));
        resultList.add(new Result().setColumn("Sub_part").setProperty("cubPart"));
        resultList.add(new Result().setColumn("Packed").setProperty("packed"));
        resultList.add(new Result().setColumn("Null").setProperty("nullValue"));
        resultList.add(new Result().setColumn("Index_type").setProperty("indexType"));
        resultList.add(new Result().setColumn("Comment").setProperty("comment"));
        resultList.add(new Result().setColumn("Index_comment").setProperty("indexComment"));
        return resultMap;
    }


}
