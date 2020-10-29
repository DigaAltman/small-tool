package com.diga.orm.controller;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.work.DatabaseGroup;
import com.diga.orm.pojo.work.User;
import com.diga.orm.service.impl.DatabaseGroupService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/database_group")
@Validated
public class DatabaseGroupController {

    @Autowired
    private DatabaseGroupService databaseGroupService;

    /**
     * 为当前用户添加数据库组
     *
     * @param user              当前用户
     * @param databaseGroupName 数据库组名称
     * @return
     */
    @PostMapping("/add")
    public ApiResponse addDatabaseGroup(User user,
                                        // 数据库组名称
                                        @Validated
                                        @NotEmpty(message = "组名称不能为空")
                                        @Range(min = 1, max = 10, message = "组名称有效长度 [1 - 10] 位") String databaseGroupName) {

        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        return databaseGroupService.addDatabaseGroup(user.getUserId(), databaseGroupName);
    }


    /**
     * 判断当前用户的数据库的组名称是否已经存在
     *
     * @param user              当前用户
     * @param databaseGroupName 数据库组名称
     * @return
     */
    @GetMapping("/exists")
    public ApiResponse databaseGroupNameExist(User user, String databaseGroupName) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        ApiResponse apiResponse = databaseGroupService.existsUserDatabaseGroupName(user.getUserId(), databaseGroupName);
        return apiResponse;
    }


    /**
     * 修改当前用户的数据库组名称
     *
     * @param user              当前用户
     * @param databaseGroupId   数据库组ID
     * @param databaseGroupName 数据库组名称
     * @param version           数据库组版本号，CAS标志锁
     * @return
     */
    @PostMapping("/edit/{databaseGroupId}")
    public ApiResponse editDataBaseGroup(User user,
                                         @PathVariable
                                         @NotEmpty(message = "数据库组ID不能为空") String databaseGroupId,

                                         @Validated
                                         @NotEmpty(message = "数据库组名称不能为空")
                                         @Range(min = 1, max = 10, message = "组名称有效长度 [1 - 10] 位") String databaseGroupName,

                                         @Validated
                                         @NotNull(message = "数据库组名称版本号不能为空") Integer version) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        return databaseGroupService.updateDataBaseGroup(user.getUserId(), databaseGroupId, databaseGroupName, version);
    }

    /**
     * 删除当前用户的指定数据库组名称的数据库组
     *
     * @param user            当前用户
     * @param databaseGroupId 数据库组ID
     * @param validationCode  数据库组名称效验码
     * @return
     */
    @PostMapping("/delete/{databaseGroupId}")
    public ApiResponse deleteDataBaseGroup(User user,
                                           @PathVariable
                                           @NotEmpty(message = "数据库组ID不能为空") String databaseGroupId,

                                           // 数据库组名称效验码,类似github,当我们删除 repository 时必须 用户名称 / repository 的库名称
                                           @Validated
                                           @NotEmpty(message = "效验码不能为空")
                                           @Range(min = 1, max = 10, message = "组名称有效长度 [1 - 10] 位") String validationCode) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        return databaseGroupService.deleteDatabaseGroup(user.getUserId(), databaseGroupId, validationCode);
    }


    /**
     * 获取当前用户的数据库组列表
     *
     * @param user 当前用户
     * @return
     */
    @GetMapping("/list")
    public ApiResponse dataBaseGroupList(User user) {
        if (user == null) {
            return ApiResponse.login("用户未登录");
        }

        List<DatabaseGroup> dataBaseVOList = databaseGroupService.getDataBaseGroupList(user.getUserId());
        return ApiResponse.success(dataBaseVOList);
    }

}
