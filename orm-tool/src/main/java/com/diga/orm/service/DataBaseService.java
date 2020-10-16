package com.diga.orm.service;

import com.diga.generic.utils.CollectionUtils;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.repository.DataBaseRepository;
import com.diga.orm.vo.DataBaseDetail;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataBaseService {

    @Autowired
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
     * 获取数据库的详细信息
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
