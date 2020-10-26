package com.diga.orm.pojo.work;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @date        2020-10-26 09:58:09
 * @description user,存储引擎:InnoDB,字符集:utf8_general_ci
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

	/**
	 * 所属索引[PRIMARY] {'类型': PRI,'结构':BTREE,'顺序':1,'备注':}
	 * 用户主键
	 */
	private	String	userId;

	/**
	 * 所属索引[username] {'类型': UNI,'结构':BTREE,'顺序':1,'备注':}
	 * 用户名称
	 */
	private	String	username;

	/**
	 * 密码
	 */
	private	String	password;

	/**
	 * 昵称
	 */
	private	String	realname;

	/**
	 * 绑定的邮箱地址
	 */
	private String emailAddress;

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
	private	BigDecimal	version;
}
