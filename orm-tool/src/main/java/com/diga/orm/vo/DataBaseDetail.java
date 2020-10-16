package com.diga.orm.vo;

import com.diga.db.annotation.Column;
import com.diga.db.annotation.ResultBean;
import com.diga.db.annotation.Transient;
import com.diga.db.core.Result;
import com.diga.db.core.ResultMap;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataBaseDetail implements Serializable {
    /**
     * 数据库名称
     */
    @Column("TABLE_SCHEMA")
    private String database;

    /**
     * 数据库端口号
     */
    private Integer port;

    /**
     * 数据库 ip 地址
     */
    private String host;

    /**
     * 数据库的字符集
     */
    private String charset;

    /**
     * 数据库的存储引擎
     */
    private String engine;

    /**
     * 数据库的版本号
     */
    private String version;

    /**
     * 数据库的大小
     */
    private BigDecimal dataSize;

    /**
     * 索引大小
     */
    private BigDecimal indexSize;

    /**
     * 缓冲区大小
     */
    private BigDecimal cacheSize;

    /**
     * 当前用户
     */
    private String username;

    /**
     * 数据库最大连接数量
     */
    private Integer maxConnectionNumber;

    /**
     * 缓存线程数
     */
    private Integer cacheThreadNumber;

    /**
     * 连接线程数
     */
    private Integer connectThreadSize;

    /**
     * 创建线程数
     */
    private Integer createdThreadSize;

    /**
     * 运行线程数
     */
    private Integer runThreadSize;

    /**
     * 数据存放服务器目录
     */
    private String dataDir;

    /**
     * 数据库下的表
     */
    private List<TableDetail> tableDetailList;

    @ResultBean(id = "com.diga.orm.vo.DataBaseDetail")
    public ResultMap dataBaseDetail() {
        ResultMap resultMap = new ResultMap(null, DataBaseDetail.class);
        List<Result> resultList = resultMap.getResultList();
        resultList.add(new Result("TABLE_SCHEMA", "database"));
        return resultMap;
    }
}
