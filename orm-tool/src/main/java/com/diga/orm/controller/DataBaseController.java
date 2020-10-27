package com.diga.orm.controller;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.common.WorkCommon;
import com.diga.orm.pojo.mysql.database.DataBaseParamValue;
import com.diga.orm.pojo.work.DatabaseGroup;
import com.diga.orm.pojo.work.User;
import com.diga.orm.repository.impl.DataBaseGroupRepository;
import com.diga.orm.service.DataBaseService;
import com.diga.orm.vo.DataBaseDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/db")
public class DataBaseController {

    @Autowired
    private DataBaseService dataBaseService;

    /**
     * 获取当前用户配置的数据库列表
     *
     * @param session
     * @return
     */
    public ApiResponse getDataBaseList(HttpSession session) {
        Object user = session.getAttribute(WorkCommon.CURRENT_USER);
        if (user == null) {
            return ApiResponse.authority("当前用户未登录,请前往登录");
        }

        User u = (User) user;

        List<DatabaseGroup> dataBaseVOList = dataBaseService.getDataBaseGroupList(u.getUserId());
        return ApiResponse.success(dataBaseVOList);
    }

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

    /**
     * 获取指定数据库对应的SQL服务器下的所有数据库信息
     *
     * @param session
     * @param databaseId 数据库ID
     * @return
     */
    @PostMapping("/list")
    public ApiResponse list(HttpSession session, String databaseId) {
        User user = (User) session.getAttribute(WorkCommon.CURRENT_USER);
        if (user == null) {
            return ApiResponse.authority("当前用户未登录");
        }

        dataBaseService.buildDataBaseToSession(databaseId, user.getUserId());
        return ApiResponse.success(dataBaseService.getAllDataBase());
    }

}
