package com.diga.db.test;

import com.diga.db.core.DB;
import com.diga.db.factory.DBFactory;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class TestDBFactory {

    private DB db;

    @Before
    public void before() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://47.112.125.251:3369/db1?useUnicode=true&characterEncoding=utf8&useSSL=false", "db1", "db1_1234");
        DBFactory dbFactory = new DBFactory(connection);
        db = dbFactory.getDB();
    }

    @Test
    public void testDBFactory()  {
        String userId = UUID.randomUUID().toString();
        Date now = new Date();
        int status = db.executeUpdate("INSERT INTO user(`user_id`, `username`, `password`, `realname`, `email_address`, `create_time`, `update_time`, `version`) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                userId, "diga", "1234", "迪迦奥特曼", null, now, now, 1);
        System.out.println(status);
    }
}
