package com.diga.orm.pojo.mysql.database;

import com.diga.db.annotation.ResultBean;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 数据库参数变量
 * show variables;
 */
@Data
public class DataBaseParamValue implements Serializable {
    private String key;
    private String value;

    @ResultBean(id = "com.diga.orm.pojo.mysql.database.DataBaseParamValue")
    public ResultMap dataBaseParamValue() {
        ResultMap resultMap = new ResultMap("", DataBaseParamValue.class);

        List<Result> resultList = resultMap.getResultList();
        resultList.add(new Result("Variable_name", "key"));
        resultList.add(new Result("Value", "value"));

        return resultMap;
    }
}
