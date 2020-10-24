package com.diga.orm.controller;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.service.ITableService;
import com.diga.orm.vo.Code;
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
    private ITableService tableService;

    @GetMapping("/structure/{tableName}")
    public ApiResponse getTableStructure(@PathVariable("tableName") String tableName) {
        TableDetail tableDetail = tableService.getTableDetail(tableName);
        return ApiResponse.success(tableDetail);
    }

    @GetMapping("/entity/{tableName}")
    public ApiResponse getEntity(@PathVariable("tableName") String tableName) {
        List<Code> codeList = tableService.generateEntity(tableName);
        return ApiResponse.success(codeList);
    }

    @GetMapping("/repository/{tableName}")
    public ApiResponse getRepository(@PathVariable("tableName") String tableName) {
        List<Code> codeList = tableService.generateRepository(tableName);
        return ApiResponse.success(codeList);
    }

}
