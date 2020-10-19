package com.diga.orm.service.impl;

import com.diga.generic.utils.CollectionUtils;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.repository.DataBaseRepository;
import com.diga.orm.repository.impl.MySQLDataBaseRepository;
import com.diga.orm.service.DataBaseService;
import com.diga.orm.vo.DataBaseDetail;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("mySQLDataBaseService")
public class MySQLDataBaseService implements DataBaseService {

    @Autowired
    @Qualifier("mySQLDataBaseRepository")
    private DataBaseRepository dataBaseRepository;


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


}
