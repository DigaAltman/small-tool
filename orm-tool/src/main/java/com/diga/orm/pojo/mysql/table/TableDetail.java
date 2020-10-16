package com.diga.orm.pojo.mysql.table;

import com.diga.db.annotation.ResultBean;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
public class TableDetail implements Serializable {
    /**
     * 数据表名称
     */
    private String tableName;

    /**
     * 对应的是实体类名称
     */
    private String entityName;

    /**
     * 数据表引擎
     */
    private String engine;

    /**
     * 数据表版本号
     */
    private BigInteger version;

    /**
     * 行格式。对于 MyISAM 引擎，这可能是Dynamic，Fixed或Compressed。
     * 动态行的行长度可变，例如Varchar或Blob类型字段。固定行是指行长度不变，例如Char和Integer类型字段
     */
    private String rowFormat;

    /**
     * 表中的行数。对于非事务性表，这个值是精确的，对于事务性引擎，这个值通常是估算的
     */
    private BigInteger rows;

    /**
     * 平均每行包括的字节数
     */
    private BigInteger avgRowLength;

    /**
     * 整个表中的数据量(字节)
     */
    private BigInteger dataLength;

    /**
     * 表可以容纳的最大数据量
     */
    private BigInteger maxDataLength;

    /**
     * 索引占用磁盘的大小
     */
    private BigInteger indexLength;

    /**
     * 对于 MyISAM 引擎，标识已分配，但现在未使用的空间，并且包含了已被删除行的空间。
     */
    private BigInteger dataFree;

    /**
     * 下一个 Auto_increment 的值
     */
    private BigInteger autoIncrement;

    /**
     * 表的创建时间
     */
    private Date createTime;

    /**
     * 表的更新时间
     */
    private Date updateTime;

    /**
     * 使用 check table 或 myisamchk 工具检查表的最近时间
     */
    private Date checkTime;

    /**
     * 表备注
     */
    private String comment;

    /**
     * 字符集
     */
    private String collation;


    @ResultBean(id = "com.diga.orm.pojo.mysql.table.TableDetail")
    public ResultMap tableDetail() {
        ResultMap resultMap = new ResultMap("", TableDetail.class);
        List<Result> resultList = resultMap.getResultList();
        resultList.add(new Result().setColumn("Name").setProperty("tableName"));
        resultList.add(new Result().setColumn("Engine").setProperty("engine"));
        resultList.add(new Result().setColumn("Version").setProperty("version"));
        resultList.add(new Result().setColumn("Row_format").setProperty("rowFormat"));
        resultList.add(new Result().setColumn("Rows").setProperty("rows"));
        resultList.add(new Result().setColumn("Avg_row_length").setProperty("avgRowLength"));
        resultList.add(new Result().setColumn("Data_length").setProperty("dataLength"));
        resultList.add(new Result().setColumn("Max_data_length").setProperty("maxDataLength"));
        resultList.add(new Result().setColumn("Index_length").setProperty("indexLength"));
        resultList.add(new Result().setColumn("Data_free").setProperty("dataFree"));
        resultList.add(new Result().setColumn("Auto_increment").setProperty("autoIncrement"));
        resultList.add(new Result().setColumn("Create_time").setProperty("createTime"));
        resultList.add(new Result().setColumn("Update_time").setProperty("updateTime"));
        resultList.add(new Result().setColumn("Check_time").setProperty("checkTime"));
        resultList.add(new Result().setColumn("Comment").setProperty("comment"));
        resultList.add(new Result().setColumn("Collation").setProperty("collation"));

        return resultMap;
    }
}
