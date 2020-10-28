package com.diga.orm.service;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.pojo.work.Database;
import com.diga.orm.pojo.work.DatabaseGroup;
import com.diga.orm.vo.DataBaseDetail;

import java.util.List;

public interface DataBaseService {

    List<DataBaseParamValue> databaseDetail();

    List<DataBaseDetail> getAllDataBase();

    DataBaseDetail databaseSimpleDetail();

    List<DatabaseGroup> getDataBaseGroupList(String userId);

    ApiResponse buildDataBaseToSession(String databaseId, String userId);

    ApiResponse add(String userId, String groupName);

    ApiResponse containsDataBaseGroup(String userId, String databaseGroupName);

    ApiResponse updateDataBaseGroup(String userId, String databaseGroupId, String databaseGroupName, int version);
}
