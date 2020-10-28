package com.diga.orm.repository.impl;

import com.diga.db.core.DB;
import com.diga.orm.pojo.work.Database;
import com.diga.orm.pojo.work.DatabaseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseRepository {

    @Autowired
    private DB db;

    /**
     * 根据 数据库id 获取数据库的信息
     *
     * @param databaseId 数据库id
     * @return
     */
    public Database selectPrimary(String databaseId) {
        return db.selectOne("SELECT `database_id`, `product_type`, `url`, `username`, `password`, `security_password`, `database_name`, `create_time`, `version` FROM `database` WHERE `database_id` = ?", Database.class, databaseId);
    }


    /**
     * 根据用户名称和数据库id获取数据库的信息
     */
    public Database selectByUserIdAndDatabaseId(String userId, String databaseId) {
        return db.selectOne("SELECT d.database_id,d.url,d.username,d.security_password,d.database_name,d.product_type,d.version,d.create_time FROM (SELECT user_id AS uid FROM `user` WHERE user_id=?) u LEFT JOIN `database_group` dg ON u.`uid`=dg.`user_id` LEFT JOIN `database` d ON dg.`database_group_id`=d.`database_group_id` WHERE d.database_id=?", Database.class, userId, databaseId);
    }

    /**
     * 插入数据库配置
     *
     * @param databaseGroup
     * @return
     */
    public int insert(DatabaseGroup databaseGroup) {
        return db.executeUpdate("INSERT INTO `database_group`(`database_group_id`, `database_group_name`, `user_id`, `create_time`, `update_time`, `version`) VALUES(?, ?, ?, now(), now(), 1)", databaseGroup.getDatabaseGroupId(), databaseGroup.getDatabaseGroupName(), databaseGroup.getUserId());
    }

}
