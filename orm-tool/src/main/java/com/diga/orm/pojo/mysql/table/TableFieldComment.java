package com.diga.orm.pojo.mysql.table;

import com.diga.db.annotation.ResultBean;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 表字段备注信息
 *
 * SELECT COLUMN_NAME AS column_name, column_comment, column_type, column_key FROM information_schema.COLUMNS WHERE table_name = #{tableName}
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableFieldComment implements Serializable {

    /**
     * 字段名称
     */
    private String columnName;

    /**
     * 字段备注
     */
    private String columnComment;

    /**
     * 字段 SQL 类型
     */
    private String columnType;

    /**
     * 字段索引, PRI, UNI, MUL
     */
    private String columnKey;

    @ResultBean(id = "com.diga.orm.pojo.mysql.table.TableFieldComment")
    public ResultMap tableFieldComment() {
        ResultMap resultMap = new ResultMap("", TableFieldComment.class);
        List<Result> resultList = resultMap.getResultList();
        resultList.add(new Result().setColumn("column_name").setProperty("columnName"));
        resultList.add(new Result().setColumn("column_comment").setProperty("columnComment"));
        resultList.add(new Result().setColumn("column_type").setProperty("columnType"));
        resultList.add(new Result().setColumn("column_key").setProperty("columnKey"));
        return resultMap;
    }

}
