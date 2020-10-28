package com.diga.orm.service.impl;

import com.diga.generic.utils.ClassUtils;
import com.diga.generic.utils.CollectionUtils;
import com.diga.orm.common.ApiResponse;
import com.diga.orm.common.WorkCommon;
import com.diga.orm.config.DatabaseManager;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.pojo.work.Database;
import com.diga.orm.pojo.work.DatabaseGroup;
import com.diga.orm.repository.DataBaseRepository;
import com.diga.orm.repository.impl.DataBaseGroupRepository;
import com.diga.orm.repository.impl.DatabaseRepository;
import com.diga.orm.repository.impl.MySQLDataBaseRepository;
import com.diga.orm.service.DataBaseService;
import com.diga.orm.vo.DataBaseDetail;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("mySQLDataBaseService")
public class MySQLDataBaseService implements DataBaseService {

    @Autowired
    @Qualifier("mySQLDataBaseRepository")
    private DataBaseRepository dataBaseRepository;

    @Autowired
    private DataBaseGroupRepository dataBaseGroupRepository;


    /**
     * 获取数据库的所有参数信息
     *
     * @return
     */
    public List<DataBaseParamValue> databaseDetail() {
        return dataBaseRepository.getDataBaseDetail();
    }


    /**
     * 获取所有数据库, 包括数据表
     *
     * @return
     */
    public List<DataBaseDetail> getAllDataBase() {

        return dataBaseRepository.getAllDataBase();
    }

    /**
     * 获取当前数据库的详细信息
     *
     * @return
     */
    public DataBaseDetail databaseSimpleDetail() {
        List<DataBaseParamValue> dataBaseParamValueList = dataBaseRepository.getDataBaseDetail();
        Map<String, String> map = new HashMap();
        CollectionUtils.forEach(dataBaseParamValueList, (index, element, elementList) -> {
            map.put(element.getKey(), element.getValue());
        });

        DataBaseDetail dataBaseDetail = new DataBaseDetail();
        dataBaseDetail.setCharset(MapUtils.getString(map, "character_set_database"));
        dataBaseDetail.setEngine(MapUtils.getString(map, "default_storage_engine"));
        dataBaseDetail.setVersion(MapUtils.getString(map, "version"));
        dataBaseDetail.setPort(MapUtils.getInteger(map, "port"));

        Map<String, BigDecimal> sizeMap = dataBaseRepository.getDataSizeAndIndexSize();
        dataBaseDetail.setDataSize(sizeMap.get("dataSize"));
        dataBaseDetail.setIndexSize(sizeMap.get("indexSize"));

        dataBaseDetail.setCacheSize(new BigDecimal(MapUtils.getString(map, "query_cache_size")));

        // TODO username
        dataBaseDetail.setMaxConnectionNumber(MapUtils.getInteger(map, "max_connections"));


        List<DataBaseParamValue> dataBaseThreadDetail = dataBaseRepository.getDataBaseThreadDetail();
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
     * 获取所有的数据库组信息,包括数据库组下的数据库信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<DatabaseGroup> getDataBaseGroupList(String userId) {
        return dataBaseGroupRepository.selectDataBaseGroupByUserId(userId);
    }

    @Autowired
    private DatabaseRepository databaseRepository;

    /**
     * 获取数据库的信息, 并设置为线程级别的共享对象
     *
     * @param databaseId
     * @return
     */
    @Override
    public ApiResponse buildDataBaseToSession(String databaseId, String userId) {
        Database database = databaseRepository.selectByUserIdAndDatabaseId(userId, databaseId);
        if (database == null) {
            return ApiResponse.validation("找不到这个数据库信息");
        }

        Connection connection = null;
        try {
            if (database.getProductType() == WorkCommon.SQLProductType.MYSQL.getCode()) {
                ClassUtils.tryForName("com.mysql.jdbc.Driver");
            } else if (database.getProductType() == WorkCommon.SQLProductType.ORACLE.getCode()) {
                ClassUtils.tryForName("oracle.JDBC.driver.OracleDriver");
            }
            connection = DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        } catch (SQLException e) {
            return ApiResponse.validation("构建数据源出错,请检查数据库配置");
        }
        DatabaseManager.set(connection);
        return ApiResponse.success();
    }


    @Override
    public ApiResponse add(String userId, String groupName) {
        DatabaseGroup databaseGroup = new DatabaseGroup();
        databaseGroup.setUserId(userId);
        databaseGroup.setDatabaseGroupId(UUID.randomUUID().toString());
        databaseGroup.setDatabaseGroupName(groupName);

        int status = databaseRepository.insert(databaseGroup);
        if(status > 0) {
            return ApiResponse.success("添加数据库成功");
        }
        return ApiResponse.server("数据库添加失败");
    }
}
