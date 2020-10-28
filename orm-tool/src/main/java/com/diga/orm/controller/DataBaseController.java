package com.diga.orm.controller;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.pojo.work.DatabaseGroup;
import com.diga.orm.pojo.work.User;
import com.diga.orm.service.DataBaseService;
import com.diga.orm.vo.DataBaseDetail;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/db")
@Validated
public class DataBaseController {

    @Autowired
    private DataBaseService dataBaseService;

    /**
     * 当前用户添加数据库组
     */
    @PostMapping("/database_group/add")
    public ApiResponse addDatabaseGroup(User user,
                                        @Validated
                                        @NotEmpty(message = "组名称不能为空")
                                        @Range(min = 1, max = 10, message = "组名称有效长度 [1 - 10] 位")
                                                String databaseGroupName) {

        if (user == null) {
            return ApiResponse.authority("用户未登录");
        }

        return dataBaseService.add(user.getUserId(), databaseGroupName);
    }

    /**
     * 判断数据库的组名称是否已经存在
     *
     * @param databaseGroupName
     * @return
     */
    @GetMapping("/database_group/exists")
    public ApiResponse databaseGroupNameExist(User user, String databaseGroupName) {
        if (user == null) {
            return ApiResponse.authority("用户未登录");
        }

        ApiResponse apiResponse = dataBaseService.containsDataBaseGroup(user.getUserId(), databaseGroupName);
        return apiResponse;
    }


    @PostMapping("/database_group/edit/{databaseGroupId}")
    public ApiResponse editDataBaseGroup(User user,

                                         @PathVariable
                                                 String databaseGroupId,

                                         @Validated
                                         @NotEmpty(message = "数据库组名称不能为空")
                                         @Range(min = 1, max = 10, message = "组名称有效长度 [1 - 10] 位")
                                                 String databaseGroupName,

                                         @Validated
                                         @NotNull(message = "数据库组名称版本号不能为空")
                                                 Integer version) {
        if (user == null) {
            return ApiResponse.authority("用户未登录");
        }

        return dataBaseService.updateDataBaseGroup(user.getUserId(), databaseGroupId, databaseGroupName, version);
    }


    @PostMapping("/database_group/delete/{databaseGroupId}")
    public ApiResponse deleteDataBaseGroup(User user,
                                           @PathVariable String databaseGroupId,

                                           // 数据库组名称效验码,类似github,当我们删除 repository 时必须 用户名称 / repository 的库名称
                                           @Validated
                                           @NotEmpty(message = "效验码不能为空")
                                           @Range(min = 1, max = 10, message = "组名称有效长度 [1 - 10] 位")
                                                   String validationCode) {
        if (user == null) {
            return ApiResponse.authority("用户未登录");
        }

        return dataBaseService.deleteDatabaseGroup(databaseGroupId);
    }


    /**
     * 获取当前用户配置的数据库列表
     *
     * @param user 当前用户
     * @return
     */
    @GetMapping("/database_group/list")
    public ApiResponse getDataBaseGroupList(User user) {
        if (user == null) {
            return ApiResponse.authority("当前用户未登录,请前往登录");
        }

        List<DatabaseGroup> dataBaseVOList = dataBaseService.getDataBaseGroupList(user.getUserId());
        return ApiResponse.success(dataBaseVOList);
    }

    /**
     * 获取当前数据库的详细信息
     *
     * @param user       当前登录用户
     * @param databaseId 数据库ID
     * @return
     */
    @GetMapping("/database/detail/{databaseId}")
    public ApiResponse detail(User user, @PathVariable String databaseId) {
        dataBaseService.buildDataBaseToSession(databaseId, user.getUserId());
        DataBaseDetail dataBaseDetail = dataBaseService.databaseSimpleDetail();
        return ApiResponse.success(dataBaseDetail);
    }

    /**
     * 获取指定数据库的参数信息
     *
     * @param user       当前登录用户
     * @param databaseId 数据库ID
     * @return
     */
    @GetMapping("/database/params/{databaseId}")
    public ApiResponse params(User user, @PathVariable String databaseId) {
        if (user == null) {
            return ApiResponse.authority("当前用户未登录");
        }

        dataBaseService.buildDataBaseToSession(databaseId, user.getUserId());
        List<DataBaseParamValue> paramValueList = dataBaseService.databaseDetail();
        return ApiResponse.success(paramValueList);
    }

    /**
     * 获取用户的指定数据库对应的SQL服务器下的所有数据库信息
     *
     * @param user       当前登录用户
     * @param databaseId 数据库ID
     * @return
     */
    @PostMapping("/database/list/{databaseId}")
    public ApiResponse list(User user, @PathVariable String databaseId) {
        if (user == null) {
            return ApiResponse.authority("当前用户未登录");
        }

        dataBaseService.buildDataBaseToSession(databaseId, user.getUserId());
        return ApiResponse.success(dataBaseService.getAllDataBase());
    }

}
