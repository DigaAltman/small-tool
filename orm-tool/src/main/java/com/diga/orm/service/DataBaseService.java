package com.diga.orm.service;

import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.vo.DataBaseDetail;

import java.util.List;

public interface DataBaseService {

    List<DataBaseParamValue> databaseDetail();

    List<DataBaseDetail> getAllDataBase();

    DataBaseDetail databaseSimpleDetail();
}
