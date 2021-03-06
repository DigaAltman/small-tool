package com.diga.orm.bo;

import com.diga.generic.utils.EncryptionUtil;
import com.diga.orm.pojo.work.Database;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@ApiModel
public class DatabaseBO implements Serializable {

    /**
     * 对应的数据库组的id
     */
    @ApiModelProperty("数据库组ID")
    private	String	databaseGroupId;

    /**
     * 数据库产品类型,0:MySQL,1:Oracle
     */
    @ApiModelProperty("数据库产品类型,0:MySQL,1:Oracle")
    private	Integer	productType;

    /**
     * 数据库的 Jdbc 或 oJdbc 地址
     */
    @ApiModelProperty("数据库的 Jdbc 或 oJdbc 地址")
    private	String	url;

    /**
     * 数据库用户名称
     */
    @ApiModelProperty("数据库用户名称")
    private	String	username;

    /**
     * 数据库用户密码
     */
    @ApiModelProperty("数据库用户密码")
    private	String	password;

    /**
     * 选择的数据库名称
     */
    @ApiModelProperty("选择的数据库名称")
    private	String	databaseName;


    /**
     * 转换为 Database
     * @return
     */
    public Database toDatabase() {
        Database database = new Database();
        database.setDatabaseId(UUID.randomUUID().toString());
        database.setDatabaseGroupId(databaseGroupId);
        database.setDatabaseName(databaseName);
        database.setUrl(url);
        database.setUsername(username);
        database.setPassword(password);
        database.setSecurityPassword(EncryptionUtil.md5(password));
        database.setProductType(productType);

        return database;
    }
}