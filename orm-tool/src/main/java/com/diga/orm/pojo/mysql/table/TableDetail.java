package com.diga.orm.pojo.mysql.table;

import com.diga.db.annotation.ResultBean;
import com.diga.db.annotation.Transient;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import com.diga.orm.vo.ColumnDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * SELECT * FROM information_schema.tables WHERE TABLE_SCHEMA != 'information_schema';
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
     * 表字符集
     */
    private String charset;

    /**
     * 表备注
     */
    private String comment;

    /**
     * 建表补充
     */
    private String options;

    @Transient
    private List<ColumnDetail> columnDetailList;

    @ResultBean(id = "com.diga.orm.pojo.mysql.table.TableDetail")
    public ResultMap tableDetail() {
        ResultMap resultMap = new ResultMap("", TableDetail.class);
        List<Result> resultList = resultMap.getResultList();

        resultList.add(new Result().setColumn("TABLE_NAME").setProperty("tableName"));
        resultList.add(new Result().setColumn("ENGINE").setProperty("engine"));
        resultList.add(new Result().setColumn("VERSION").setProperty("version"));
        resultList.add(new Result().setColumn("ROW_FORMAT").setProperty("rowFormat"));
        resultList.add(new Result().setColumn("TABLE_ROWS").setProperty("rows"));
        resultList.add(new Result().setColumn("AVG_ROW_LENGTH").setProperty("avgRowLength"));
        resultList.add(new Result().setColumn("DATA_LENGTH").setProperty("dataLength"));
        resultList.add(new Result().setColumn("MAX_DATA_LENGTH").setProperty("maxDataLength"));
        resultList.add(new Result().setColumn("INDEX_LENGTH").setProperty("indexLength"));
        resultList.add(new Result().setColumn("DATA_FREE").setProperty("dataFree"));
        resultList.add(new Result().setColumn("AUTO_INCREMENT").setProperty("autoIncrement"));
        resultList.add(new Result().setColumn("CREATE_TIME").setProperty("createTime"));
        resultList.add(new Result().setColumn("UPDATE_TIME").setProperty("updateTime"));
        resultList.add(new Result().setColumn("CHECK_TIME").setProperty("checkTime"));
        resultList.add(new Result().setColumn("TABLE_COLLATION").setProperty("charset"));
        resultList.add(new Result().setColumn("TABLE_COMMENT").setProperty("comment"));
        resultList.add(new Result().setColumn("CREATE_OPTIONS").setProperty("options"));

        return resultMap;
    }
}
