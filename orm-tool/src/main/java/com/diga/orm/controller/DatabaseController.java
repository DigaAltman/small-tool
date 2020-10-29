package com.diga.orm.controller;

import com.diga.orm.bo.DatabaseBO;
import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.pojo.work.User;
import com.diga.orm.service.impl.DatabaseService;
import com.diga.orm.vo.DataBaseDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/database")
@Validated
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    /**
     * 在指定的数据库组下添加数据库信息
     *
     * @param user       当前登录用户
     * @param databaseBO 数据库信息
     * @return
     */
    @PostMapping("/add")
    public ApiResponse add(User user, @RequestBody DatabaseBO databaseBO) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        return databaseService.addDataBase(databaseBO);
    }

    /**
     * 获取当前数据库的详细信息
     *
     * @param user       当前登录用户
     * @param databaseId 数据库ID
     * @return
     */
    @GetMapping("/detail/{databaseId}")
    public ApiResponse detail(User user, @PathVariable String databaseId) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        ApiResponse setSessionResponse = databaseService.buildDataBaseToSessionDB(databaseId, user.getUserId());
        if (!setSessionResponse.statusSuccess()) {
            return setSessionResponse;
        }

        DataBaseDetail dataBaseDetail = databaseService.getDatabaseDetail();
        return ApiResponse.success(dataBaseDetail);
    }

    /**
     * 获取指定数据库的参数信息
     *
     * @param user       当前登录用户
     * @param databaseId 数据库ID
     * @return
     */
    @GetMapping("/params/{databaseId}")
    public ApiResponse params(User user, @PathVariable String databaseId) {
        if (user == null) {
            return ApiResponse.authority("用户未登录");
        }

        ApiResponse status = databaseService.buildDataBaseToSessionDB(databaseId, user.getUserId());
        if (!status.statusSuccess()) {
            return status;
        }

        List<DataBaseParamValue> paramValueList = databaseService.getDataBaseParamList();
        return ApiResponse.success(paramValueList);
    }

}
