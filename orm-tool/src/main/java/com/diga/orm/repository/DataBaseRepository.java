package com.diga.orm.repository;

import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.vo.DataBaseDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DataBaseRepository {

    List<DataBaseParamValue> getDataBaseDetail();

    Map<String, BigDecimal> getDataSizeAndIndexSize();

    List<DataBaseParamValue> getDataBaseThreadDetail();

    List<DataBaseDetail> getAllDataBase();
}
