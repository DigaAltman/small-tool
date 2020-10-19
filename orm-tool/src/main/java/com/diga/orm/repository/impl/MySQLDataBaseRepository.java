package com.diga.orm.repository.impl;

import com.diga.db.core.DB;
import com.diga.orm.configuration.ResultMapFactoryBean;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.repository.DataBaseRepository;
import com.diga.orm.vo.DataBaseDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository("mySQLDataBaseRepository")
public class MySQLDataBaseRepository implements DataBaseRepository {

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

    // 获取所有数据库, 包括下面的表信息
    public List<DataBaseDetail> getAllDataBase() {
        List<DataBaseDetail> dataBaseDetailList = db.selectList("SELECT TABLE_SCHEMA, TABLE_NAME, ENGINE, VERSION, ROW_FORMAT, TABLE_ROWS, AVG_ROW_LENGTH, DATA_LENGTH, MAX_DATA_LENGTH, INDEX_LENGTH, DATA_FREE, AUTO_INCREMENT, CREATE_TIME, UPDATE_TIME, CHECK_TIME, TABLE_COLLATION, TABLE_COMMENT, CREATE_OPTIONS  FROM information_schema.tables WHERE TABLE_SCHEMA != 'information_schema'", DataBaseDetail.class);
        return dataBaseDetailList;
    }

}
