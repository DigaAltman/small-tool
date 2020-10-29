package com.diga.orm.controller;

import com.diga.db.core.DB;
import com.diga.generic.utils.CollectionUtils;
import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.work.User;
import com.diga.orm.repository.SqlRepository;
import com.diga.orm.service.impl.DatabaseService;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sql")
@ApiOperation("SQL在线测试接口")
public class SQLController {

    @Autowired
    private DB db;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private SqlRepository sqlRepository;

    @GetMapping("/{sql}")
    @ApiOperation("此项目下的SQL环境,只允许SELECT而不允许UPDATE,DELETE,CREATE请求. 测试环境下开放")
    public ApiResponse devSelectSQL(@ApiParam("SQL语句") @PathVariable("sql") String sql) {
        List<Map> mapList = db.selectList(sql, Map.class);
        return ApiResponse.success(mapList);
    }


    @ApiOperation("用户数据库环境下执行SQL")
    @PostMapping("/{databaseId}/{sql}")
    public ApiResponse executeSQL(User user, @ApiParam("SQL语句") @PathVariable String databaseId, @PathVariable String sql) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }
        databaseService.buildDataBaseToSessionDB(databaseId, user.getUserId());
        List<String> executePrefix = Lists.newArrayList("UPDATE", "DELETE", "INSERT", "CREATE", "TRUNCATE", "ALTER");

        boolean status = executePrefix.parallelStream().anyMatch(prefix -> sql.trim().startsWith(prefix) || sql.trim().startsWith(prefix.toLowerCase()));
        if (status) {
            return ApiResponse.success(sqlRepository.excuteUpdate(sql));
        }
        return ApiResponse.success(sqlRepository.executeSelect(sql));
    }

}
