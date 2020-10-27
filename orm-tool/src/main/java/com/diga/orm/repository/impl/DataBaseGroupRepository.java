package com.diga.orm.repository.impl;

import com.diga.db.core.DB;
import com.diga.orm.pojo.work.DatabaseGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataBaseGroupRepository {

    @Autowired
    private DB db;

    /**
     * 根据用户id获取用户配置的数据库组id
     * @param userId
     * @return
     */
    public List<DatabaseGroup> selectDataBaseGroupByUserId(String userId) {
        return db.selectList("SELECT dg.`database_group_id`,dg.`database_group_name`,d.`product_type`,d.`url`,d.`username`,d.`password`,d.`database_name`,d.`security_password` FROM `database_group` dg LEFT JOIN `database` d ON dg.`database_group_id`=d.`database_group_id` WHERE `user_id`=?",
                DatabaseGroup.class, userId);
    }
}
