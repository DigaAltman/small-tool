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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
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
    @PostMapping("/add")
    public ApiResponse addDataBaseGroup(
            User user,

            @Validated
            @NotEmpty(message = "组名称不能为空")
            @Range(min = 1, max = 10, message = "组名称有效长度 [1 - 10] 位")
            String groupName) {

        if(user == null) {
            return ApiResponse.authority("用户未登录");
        }

        return dataBaseService.add(user.getUserId(), groupName);

    }

    /**
     * 获取当前用户配置的数据库列表
     *
     * @param user 当前用户
     * @return
     */
    public ApiResponse getDataBaseList(User user) {
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
    @GetMapping("/detail")
    public ApiResponse detail(User user, String databaseId) {
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
    @GetMapping("/params")
    public ApiResponse params(User user, String databaseId) {
        if (user == null) {
            return ApiResponse.authority("当前用户未登录");
        }

        dataBaseService.buildDataBaseToSession(databaseId, user.getUserId());
        List<DataBaseParamValue> paramValueList = dataBaseService.databaseDetail();
        return ApiResponse.success(paramValueList);
    }

    /**
     * 获取指定数据库对应的SQL服务器下的所有数据库信息
     *
     * @param user       当前登录用户
     * @param databaseId 数据库ID
     * @return
     */
    @PostMapping("/list")
    public ApiResponse list(User user, String databaseId) {
        if (user == null) {
            return ApiResponse.authority("当前用户未登录");
        }

        dataBaseService.buildDataBaseToSession(databaseId, user.getUserId());
        return ApiResponse.success(dataBaseService.getAllDataBase());
    }

}
