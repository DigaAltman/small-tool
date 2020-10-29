package com.diga.orm.repository;

import com.diga.db.core.DB;
import com.diga.orm.annotation.SetDB;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SqlRepository {

    private DB db;

    @SetDB
    public List<Map> executeSelect(String sql) {
        return db.selectList(sql, Map.class);
    }

    @SetDB
    public int excuteUpdate(String sql) {
        return db.executeUpdate(sql);
    }

}
