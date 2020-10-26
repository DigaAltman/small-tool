package com.diga.orm.repository.impl;

import com.diga.db.core.DB;
import com.diga.orm.pojo.work.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private DB db;


    /**
     * 根据用户名查询用户,此方法走唯一索引
     *
     * @param username
     * @return
     */
    public User selectByUserName(String username) {
        User user = db.selectOne("SELECT `user_id`, `username`, `password`, `realname`, `email_address`, `create_time`, `update_time`, `version` FROM `user` WHERE `username` = ?", User.class, username);
        return user;
    }

    /**
     * 插入数据
     *
     * @param user
     * @return
     */
    public int insert(User user) {
        int status = db.executeUpdate("INSERT INTO `user`(`user_id`, `username`, `password`, `realname`, `email_address`, `create_time`, `update_time`, `version`) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                user.getUserId(), user.getUsername(), user.getPassword(), user.getRealname(), user.getEmailAddress(), user.getCreateTime(), user.getUpdateTime(), 1);
        return status;
    }

    /**
     * 修改数据
     * @param user
     * @return
     */
    public int update(User user) {
        com.diga.generic.utils.StringUtils.SBuilder sql = com.diga.generic.utils.StringUtils.to(" UPDATE `user` SET 1=1");
        List paramList = new ArrayList();

        if (user != null) {
            if (StringUtils.isBlank(user.getPassword())) {
                sql.to(",password=?");
                paramList.add(user.getPassword());
            }
            if (StringUtils.isBlank(user.getRealname())) {
                sql.to(",realname=?");
                paramList.add(user.getRealname());
            }
            if(StringUtils.isBlank(user.getEmailAddress())) {
                sql.to(",email_address=?");
                paramList.add(user.getEmailAddress());
            }
        }

        sql.to("update_time = now(), version = version + 1 WHERE user_id=? AND version = ? LIMIT 1");
        paramList.add(user.getUserId());
        paramList.add(user.getVersion());
        return db.executeUpdate(sql.toString(), paramList.toArray());
    }
}
