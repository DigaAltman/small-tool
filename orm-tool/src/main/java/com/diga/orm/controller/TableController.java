package com.diga.orm.controller;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.mysql.table.TableDetail;
import com.diga.orm.pojo.work.User;
import com.diga.orm.service.impl.DatabaseService;
import com.diga.orm.service.impl.TableService;
import com.diga.orm.vo.Code;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/table")
@Validated
@Api(value = "表", tags = "表接口")
public class TableController {

    @Autowired
    private TableService tableService;

    @Autowired
    private DatabaseService dataBaseService;

    /**
     * 获取指定数据库下的表的结构
     *
     * @param user       当前用户
     * @param databaseId 数据库ID
     * @param tableName  数据表名称
     * @return
     */
    @PostMapping("/structure/{databaseId}/{tableName}")
    @ApiOperation("获取指定数据库下的表的结构")
    public ApiResponse getTableStructure(User user, @ApiParam("数据库ID") @PathVariable String databaseId, @ApiParam("数据表名称") @PathVariable String tableName) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        ApiResponse status = dataBaseService.buildDataBaseToSessionDB(databaseId, user.getUserId());
        if (!status.statusSuccess()) {
            return status;
        }

        TableDetail tableDetail = tableService.getTableDetail(tableName);
        return ApiResponse.success(tableDetail);
    }

    @GetMapping("/entity/{databaseId}/{tableName}")
    @ApiOperation("获取数据表对应的实体类")
    public ApiResponse getEntity(User user, @ApiParam("数据库ID") @PathVariable String databaseId, @ApiParam("数据表名称") @PathVariable String tableName) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        ApiResponse status = dataBaseService.buildDataBaseToSessionDB(databaseId, user.getUserId());
        if (!status.statusSuccess()) {
            return status;
        }

        List<Code> codeList = tableService.generateEntity(tableName);
        return ApiResponse.success(codeList);
    }

    @GetMapping("/repository/{databaseId}/{tableName}")
    @ApiOperation("获取数据表对应的持久层代码")
    public ApiResponse getRepository(User user, @ApiParam("数据库ID") @PathVariable String databaseId, @ApiParam("数据表名称") @PathVariable String tableName) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        ApiResponse status = dataBaseService.buildDataBaseToSessionDB(databaseId, user.getUserId());
        if (!status.statusSuccess()) {
            return status;
        }

        List<Code> codeList = tableService.generateRepository(tableName);
        return ApiResponse.success(codeList);
    }

}
