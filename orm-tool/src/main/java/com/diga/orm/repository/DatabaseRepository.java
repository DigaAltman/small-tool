package com.diga.orm.repository;

import com.diga.db.core.DB;
import com.diga.orm.pojo.work.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseRepository {

    @Autowired
    private DB db;

    /**
     * 根据 主键 获取数据库的信息
     *
     * @param primary 主键
     * @return
     */
    public Database selectPrimary(String primary) {
        return db.selectOne("SELECT `database_id`, `product_type`, `url`, `username`, `password`, `security_password`, `database_name`, `create_time`, `version` FROM `database` WHERE `database_id` = ?", Database.class, primary);
    }


    /**
     * 根据 用户ID 和 数据库ID 获取数据库的信息
     *
     * @param userId     用户ID
     * @param databaseId 数据库ID
     * @return
     */
    public Database selectByUserIdAndDatabaseId(String userId, String databaseId) {
        return db.selectOne("SELECT d.database_id,d.url,d.username,d.security_password,d.database_name,d.product_type,d.version,d.create_time FROM (SELECT user_id AS uid FROM `user` WHERE user_id=?) u LEFT JOIN `database_group` dg ON u.`uid`=dg.`user_id` LEFT JOIN `database` d ON dg.`database_group_id`=d.`database_group_id` WHERE d.database_id=?", Database.class, userId, databaseId);
    }

    /**
     * 插入数据库数据到数据库中
     *
     * @param database 数据库信息
     * @return
     */
    public int insert(Database database) {
        return db.executeUpdate("INSERT `database`(`database_id`,`database_group_id`,`database_name`,`url`, `username`, `password`, `security_password`, `product_type`, `create_time`, `update_time`,`version`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, now(), now(), 1)",
                database.getDatabaseId(), database.getDatabaseGroupId(), database.getDatabaseName(), database.getUrl(), database.getUsername(), database.getPassword(), database.getProductType());
    }

}
