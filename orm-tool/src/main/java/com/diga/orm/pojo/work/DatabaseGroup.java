package com.diga.orm.pojo.work;

import com.diga.db.annotation.Transient;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @date        2020-10-26 14:53:21
 * @description database_group,存储引擎:InnoDB,字符集:utf8_general_ci
 */
@Data
public class DatabaseGroup implements Serializable {

    /**
     * 所属索引[PRIMARY] {'类型': PRI,'结构':BTREE,'顺序':1,'备注':}
     * 数据库组id
     */
    private	String	databaseGroupId;

    /**
     * 唯一索引
     * 数据库组对应名称
     */
    private String databaseGroupName;

    /**
     * 对应的用户id
     */
    private	String	userId;

    /**
     * 创建时间
     */
    private	Date	createTime;

    /**
     * 更新时间
     */
    private	Date	updateTime;

    /**
     * 版本号
     */
    @Transient
    private	BigDecimal	version;

    /**
     * 数据库组下的数据库名称
     */
    private List<Database> databaseList = new ArrayList();
}