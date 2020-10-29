package com.diga.orm.service.impl;

import com.diga.generic.utils.ClassUtils;
import com.diga.generic.utils.CollectionUtils;
import com.diga.orm.bo.DatabaseBO;
import com.diga.orm.common.ApiResponse;
import com.diga.orm.common.WorkCommon;
import com.diga.orm.config.DatabaseManager;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.pojo.work.Database;
import com.diga.orm.repository.ConnectionManagerRepository;
import com.diga.orm.repository.DatabaseRepository;
import com.diga.orm.vo.DataBaseDetail;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private ConnectionManagerRepository connectionManagerRepository;

    /**
     * 添加数据库信息
     *
     * @param databaseBO 数据库信息
     * @return
     */
    public ApiResponse addDataBase(DatabaseBO databaseBO) {
        Database database = databaseBO.toDatabase();
        int status = databaseRepository.insert(database);

        if (status > 0) {
            return ApiResponse.success("数据库添加成功");
        }

        return ApiResponse.server("数据库添加失败");
    }

    /**
     * 将当前用户配置的数据库ID中的数据库信息加载到当前会话中, 从而实现会话级别数据源
     *
     * @param databaseId 数据库ID
     * @param userId     用户ID
     */
    public ApiResponse buildDataBaseToSessionDB(String databaseId, String userId) {
        Database database = databaseRepository.selectByUserIdAndDatabaseId(userId, databaseId);
        if (database == null) {
            return ApiResponse.error("找不到这个数据库信息");
        }

        Connection connection = null;
        try {
            // 初始化数据源
            if (database.getProductType() == WorkCommon.SQLProductType.MYSQL.getCode()) {
                ClassUtils.tryForName("com.mysql.jdbc.Driver");
            } else if (database.getProductType() == WorkCommon.SQLProductType.ORACLE.getCode()) {
                ClassUtils.tryForName("oracle.JDBC.driver.OracleDriver");
            }

            // 激活数据库会话 Connection 对象
            connection = DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());

            DatabaseManager.set(connection);
            return ApiResponse.success();
        } catch (SQLException e) {
            return ApiResponse.server("构建数据源出错,请检查数据库配置");
        }
    }

    /**
     * 获取当前用户选择的数据库的详细信息
     *
     * @return
     */
    public DataBaseDetail getDatabaseDetail() {
        List<DataBaseParamValue> dataBaseParamValueList = connectionManagerRepository.getDataBaseDetail();
        Map<String, String> map = new HashMap();
        CollectionUtils.forEach(dataBaseParamValueList, (index, element, elementList) -> {
            map.put(element.getKey(), element.getValue());
        });

        DataBaseDetail dataBaseDetail = new DataBaseDetail();
        dataBaseDetail.setCharset(MapUtils.getString(map, "character_set_database"));
        dataBaseDetail.setEngine(MapUtils.getString(map, "default_storage_engine"));
        dataBaseDetail.setVersion(MapUtils.getString(map, "version"));
        dataBaseDetail.setPort(MapUtils.getInteger(map, "port"));

        Map<String, BigDecimal> sizeMap = connectionManagerRepository.getDataSizeAndIndexSize();
        dataBaseDetail.setDataSize(sizeMap.get("dataSize"));
        dataBaseDetail.setIndexSize(sizeMap.get("indexSize"));

        dataBaseDetail.setCacheSize(new BigDecimal(MapUtils.getString(map, "query_cache_size")));

        // TODO username
        dataBaseDetail.setMaxConnectionNumber(MapUtils.getInteger(map, "max_connections"));


        List<DataBaseParamValue> dataBaseThreadDetail = connectionManagerRepository.getDataBaseThreadDetail();
        Map<String, String> threadMap = new HashMap();
        CollectionUtils.forEach(dataBaseThreadDetail, (index, element, elementList) -> {
            threadMap.put(element.getKey(), element.getValue());
        });

        dataBaseDetail.setCacheThreadNumber(MapUtils.getInteger(threadMap, "Threads_cached"));
        dataBaseDetail.setConnectThreadSize(MapUtils.getInteger(threadMap, "Threads_connected"));
        dataBaseDetail.setCreatedThreadSize(MapUtils.getInteger(threadMap, "Threads_created"));
        dataBaseDetail.setRunThreadSize(MapUtils.getInteger(threadMap, "Threads_running"));

        dataBaseDetail.setDataDir(MapUtils.getString(map, "datadir"));
        return dataBaseDetail;
    }


    /**
     * 获取当前数据库的参数信息
     *
     * @return
     */
    public List<DataBaseParamValue> getDataBaseParamList() {
        return connectionManagerRepository.getDataBaseDetail();
    }

}
