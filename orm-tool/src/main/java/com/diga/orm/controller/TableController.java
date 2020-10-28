package com.diga.orm.controller;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.pojo.work.User;
import com.diga.orm.service.DataBaseService;
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

    @Autowired
    private DataBaseService dataBaseService;

    /**
     * 获取指定数据库下的表的结构
     *
     * @param user
     * @param databaseId 数据库id
     * @param tableName  表名称
     * @return
     */
    @GetMapping("/structure/{tableName}")
    public ApiResponse getTableStructure(User user, String databaseId, @PathVariable("tableName") String tableName) {
        if (user == null) {
            return ApiResponse.login("当前用户未登录,请先登录");
        }

        dataBaseService.buildDataBaseToSession(databaseId, user.getUserId());
        TableDetail tableDetail = tableService.getTableDetail(tableName);
        return ApiResponse.success(tableDetail);
    }

    @GetMapping("/entity/{tableName}")
    public ApiResponse getEntity(User user, String databaseId, @PathVariable("tableName") String tableName) {
        if (user == null) {
            return ApiResponse.login("当前用户未登录,请先登录");
        }

        dataBaseService.buildDataBaseToSession(databaseId, user.getUserId());
        List<Code> codeList = tableService.generateEntity(tableName);
        return ApiResponse.success(codeList);
    }

    @GetMapping("/repository/{tableName}")
    public ApiResponse getRepository(User user, String databaseId, @PathVariable("tableName") String tableName) {
        if (user == null) {
            return ApiResponse.login("当前用户未登录,请先登录");
        }

        dataBaseService.buildDataBaseToSession(databaseId, user.getUserId());
        List<Code> codeList = tableService.generateRepository(tableName);
        return ApiResponse.success(codeList);
    }

}
