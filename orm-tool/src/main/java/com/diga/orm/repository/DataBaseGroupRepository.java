package com.diga.orm.repository;

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
     * @param primary 主键
     * @return
     */
    public DatabaseGroup selectPrimary(String primary) {
        return db.selectOne("SELECT `database_group_id`,`database_group_name`,`user_id`,`create_time`,`update_time`,`version` FROM `database_group` WHERE `database_group_id`=?", DatabaseGroup.class, primary);
    }

    /**
     * 基于版本号修改数据
     *
     * @param databaseGroup 数据库组对象
     * @return
     */
    public int updateByVersion(DatabaseGroup databaseGroup) {
        return db.executeUpdate("UPDATE `database_group` SET `database_group_name` = ?,`update_time` = now(),`version` = `version` + 1 WHERE `database_group_id` = ? AND `version` = ?", databaseGroup.getDatabaseGroupName(), databaseGroup.getDatabaseGroupId(), databaseGroup.getVersion());
    }

    /**
     * 基于主键删除数据库组
     *
     * @param primary 数据库主键
     * @return
     */
    public int deletePrimary(String primary) {
        return db.executeUpdate("DELETE FROM `database_group` WHERE `database_group_id` = ? LIMIT 1", primary);
    }


    /**
     * 插入数据库组数据
     *
     * @param databaseGroup
     * @return
     */
    public int insert(DatabaseGroup databaseGroup) {
        return db.executeUpdate("INSERT INTO `database_group`(`database_group_id`, `database_group_name`, `user_id`, `create_time`, `update_time`, `version`) VALUES(?, ?, ?, now(), now(), 1)", databaseGroup.getDatabaseGroupId(), databaseGroup.getDatabaseGroupName(), databaseGroup.getUserId());
    }

    /**
     * 根据用户id获取用户配置的数据库组id
     *
     * @param userId 用户ID
     * @return
     */
    public List<DatabaseGroup> selectByUserId(String userId) {
        return db.selectList("SELECT `database_group_id`, `database_group_name`, `user_id`, `version`, `create_time`, `update_time` FROM `database_group` WHERE `user_id`=?", DatabaseGroup.class, userId);
    }

}
