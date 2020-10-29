package com.diga.orm.repository;

import com.diga.db.core.DB;
import com.diga.orm.annotation.SetDB;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.vo.DataBaseDetail;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public class ConnectionManagerRepository {

    private DB db;

    // 获取数据库详细信息
    @SetDB
    public List<DataBaseParamValue> getDataBaseDetail() {
        return db.selectList("show variables", DataBaseParamValue.class);
    }

    // 获取数据库的大小
    @SetDB
    public Map<String, BigDecimal> getDataSizeAndIndexSize() {
        return db.selectOne("SELECT SUM(data_length) AS dataSize,  SUM(index_length) AS indexSize FROM information_schema.tables", Map.class);
    }

    // 获取数据库线程想关信息
    @SetDB
    public List<DataBaseParamValue> getDataBaseThreadDetail() {
        return db.selectList("show status like 'Threads%'", DataBaseParamValue.class);
    }

    // 获取所有数据库, 包括下面的表信息
    @SetDB
    public List<DataBaseDetail> getAllDataBase() {
        List<DataBaseDetail> dataBaseDetailList = db.selectList("SELECT TABLE_SCHEMA, TABLE_NAME, ENGINE, VERSION, ROW_FORMAT, TABLE_ROWS, AVG_ROW_LENGTH, DATA_LENGTH, MAX_DATA_LENGTH, INDEX_LENGTH, DATA_FREE, AUTO_INCREMENT, CREATE_TIME, UPDATE_TIME, CHECK_TIME, TABLE_COLLATION, TABLE_COMMENT, CREATE_OPTIONS  FROM information_schema.tables WHERE TABLE_SCHEMA != 'information_schema'", DataBaseDetail.class);
        return dataBaseDetailList;
    }


}
