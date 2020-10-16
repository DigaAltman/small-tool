package com.diga.orm.controller;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.service.TableService;
import com.diga.orm.vo.ColumnDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tab")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping("/structure/{tableName}")
    public ApiResponse getTableStructure(@PathVariable("tableName") String tableName) {
        List<ColumnDetail> columnDetailList = tableService.getTableColumnList(tableName);
        return ApiResponse.success(columnDetailList);
    }

    @GetMapping("/entity/{tableName}")
    public ApiResponse getEntity(@PathVariable("tableName") String tableName) {
        tableService.generateEntity(tableName);
        return ApiResponse.success("请求成功!!");
    }

    @GetMapping("/repository/{tableName}")
    public ApiResponse getRepository(@PathVariable("tableName") String tableName) {
        tableService.generateRepository(tableName);
        return ApiResponse.success("请求成功!!");
    }

}
