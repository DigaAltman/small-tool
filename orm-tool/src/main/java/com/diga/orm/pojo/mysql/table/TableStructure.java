package com.diga.orm.pojo.mysql.table;

import com.diga.db.annotation.ResultBean;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表结构信息, 通过调用 show columns from #{tableName} 得到的结果对应的实体类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableStructure implements Serializable {
    /**
     * 字段名称
     */
    private String Field;

    /**
     * 字段的 SQL 类型
     */
    private String Type;

    /**
     * 字段是否为 NULL
     */
    private String Null;

    /**
     * 字段是否为 主键
     */
    private String Key;

    /**
     * 字段默认值
     */
    private String Default;

    /**
     * 字段扩展补充
     */
    private String Extra;

    @ResultBean(id = "com.diga.orm.pojo.mysql.table.TableStructure")
    public ResultMap tableStructure() {
        ResultMap resultMap = new ResultMap("", TableStructure.class);
        List<Result> resultList = resultMap.getResultList();
        resultList.add(new Result().setColumn("Field").setProperty("Field"));
        resultList.add(new Result().setColumn("Type").setProperty("Type"));
        resultList.add(new Result().setColumn("Null").setProperty("Null"));
        resultList.add(new Result().setColumn("Key").setProperty("Key"));
        resultList.add(new Result().setColumn("Default").setProperty("Default"));
        resultList.add(new Result().setColumn("Extra").setProperty("Extra"));
        return resultMap;
    }
}


