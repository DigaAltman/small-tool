package com.diga.orm.repository.impl;

import com.diga.db.core.DB;
import com.diga.generic.utils.StringUtils;
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
     *
     * @param userId
     * @return
     */
    public List<DatabaseGroup> selectDataBaseGroupByUserId(String userId) {
        return db.selectList("SELECT dg.`database_group_id`,dg.`database_group_name`,d.`product_type`,d.`url`,d.`username`,d.`password`,d.`database_name`,d.`security_password` FROM `database_group` dg LEFT JOIN `database` d ON dg.`database_group_id`=d.`database_group_id` WHERE `user_id`=?",
                DatabaseGroup.class, userId);
    }

    /**
     * 查询数据库中指定用户是否已经存在对应的数据库组名称
     *
     * @param userId
     * @param databaseGroupName
     * @return
     */
    public int containsByUserIdAndDataBaseGroupName(String userId, String databaseGroupName) {
        return db.selectOne("SELECT count(1) FROM `database_group` WHERE database_group_name=? AND user_id=?", int.class, databaseGroupName, userId);
    }

    /**
     * 根据主键查询数据库组
     *
     * @param databaseGroupId 数据库组ID
     * @return
     */
    public DatabaseGroup selectPrimary(String databaseGroupId) {
        return db.selectOne("SELECT `database_group_id`,`database_group_name`,`user_id`,`create_time`,`update_time`,`version` FROM `database_group` WHERE `database_group_id`=?", DatabaseGroup.class, databaseGroupId);
    }

    /**
     * 基于版本号修改数据
     *
     * @param databaseGroup 数据库组对象
     * @param version       数据库组版本号
     * @return
     */
    public int updateByVersion(DatabaseGroup databaseGroup, int version) {
        return db.executeUpdate("UPDATE `database_group` SET `database_group_name` = ?,`update_time` = now(),`version` = `version` + 1 WHERE `database_group_id` = ? AND `version` = ?", databaseGroup.getDatabaseGroupName(), databaseGroup.getDatabaseGroupId(), version);
    }
}
