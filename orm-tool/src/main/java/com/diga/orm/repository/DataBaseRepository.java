package com.diga.orm.repository;

import com.diga.db.core.DB;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class DataBaseRepository {

    @Autowired
    private DB db;

    // 获取数据库详细信息
    public List<DataBaseParamValue> getDataBaseDetail() {
        return db.selectList("show variables", DataBaseParamValue.class);
    }

    // 获取数据库的大小
    public Map<String, BigDecimal> getDataSizeAndIndexSize() {
        return db.selectOne("SELECT SUM(data_length) AS dataSize,  SUM(index_length) AS indexSize FROM information_schema.tables", Map.class);
    }

    // 获取数据库线程想关信息
    public List<DataBaseParamValue> getDataBaseThreadDetail() {
        return db.selectList("show status like 'Threads%'", DataBaseParamValue.class);
    }

}
