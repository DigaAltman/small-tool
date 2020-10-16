package com.diga.orm.controller;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.service.DataBaseService;
import com.diga.orm.vo.DataBaseDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/db")
public class DataBaseController {

    @Autowired
    private DataBaseService dataBaseService;

    @GetMapping("/detail")
    public ApiResponse detail() {
        DataBaseDetail dataBaseDetail = dataBaseService.databaseSimpleDetail();
        return ApiResponse.success(dataBaseDetail);
    }

    @GetMapping("/params")
    public ApiResponse params() {
        List<DataBaseParamValue> paramValueList = dataBaseService.databaseDetail();
        return ApiResponse.success(paramValueList);
    }

    @GetMapping("/list")
    public ApiResponse list() {
        List<DataBaseDetail> allDataBaseList = dataBaseService.getAllDataBase();
        return ApiResponse.success(allDataBaseList);
    }

}
