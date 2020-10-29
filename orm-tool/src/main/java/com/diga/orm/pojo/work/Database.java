package com.diga.orm.pojo.work;

import com.diga.db.annotation.Transient;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * @date        2020-10-26 15:07:28
 * @description database,存储引擎:InnoDB,字符集:utf8_general_ci
 */
@Data
public class Database implements Serializable {

    /**
     * 所属索引[PRIMARY] {'类型': PRI,'结构':BTREE,'顺序':1,'备注':}
     * 数据库的id
     */
    private	String	databaseId;

    /**
     * 对应的数据库组的id
     */
    private	String	databaseGroupId;

    /**
     * 数据库产品类型,0:MySQL,1:Oracle
     */
    private	Integer	productType;

    /**
     * 数据库的jdbc或ojdbc地址
     */
    private	String	url;

    /**
     * 数据库用户名称
     */
    private	String	username;

    /**
     * 数据库用户密码
     */
    private	String	password;

    /**
     * 选择的数据库名称
     */
    private	String	databaseName;

    /**
     * 数据库密码加密后
     */
    private	String	securityPassword;

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
}