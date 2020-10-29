package com.diga.orm.service.impl;

import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.work.DatabaseGroup;
import com.diga.orm.repository.DataBaseGroupRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class DatabaseGroupService {

    @Autowired
    private DataBaseGroupRepository dataBaseGroupRepository;

    /**
     * 为指定用户添加数据库组数据
     *
     * @param userId            用户ID
     * @param databaseGroupName 数据库组名称
     * @return
     */
    @Transactional
    public ApiResponse addDatabaseGroup(String userId, String databaseGroupName) {
        DatabaseGroup databaseGroup = new DatabaseGroup();
        databaseGroup.setUserId(userId);
        databaseGroup.setDatabaseGroupId(UUID.randomUUID().toString());
        databaseGroup.setDatabaseGroupName(databaseGroupName);

        int status = dataBaseGroupRepository.insert(databaseGroup);
        if (status > 0) {
            return ApiResponse.success("添加数据库成功");
        }
        return ApiResponse.server("数据库添加失败");
    }

    /**
     * 判断用户是否已经存在这个数据库组名称
     *
     * @param userId            用户ID
     * @param databaseGroupName 数据库组名称
     * @return
     */
    public ApiResponse existsUserDatabaseGroupName(String userId, String databaseGroupName) {
        int count = dataBaseGroupRepository.containsByUserIdAndDataBaseGroupName(userId, databaseGroupName);
        if (count > 0) {
            return ApiResponse.validation("数据库组名称已经存在!!");
        }
        return ApiResponse.success();
    }


    /**
     * 修改指定用户的数据库组名称
     *
     * @param userId            用户ID
     * @param databaseGroupId   数据库组ID
     * @param databaseGroupName 数据库组名称
     * @param version           数据库组版本号
     * @return
     */
    @Transactional
    public ApiResponse updateDataBaseGroup(String userId, String databaseGroupId, String databaseGroupName, Integer version) {
        DatabaseGroup databaseGroup = dataBaseGroupRepository.selectPrimary(databaseGroupId);
        if (databaseGroup == null) {
            return ApiResponse.error("数据库组不存在");
        }

        // 涉及到横向越权
        if (!StringUtils.equals(databaseGroup.getUserId(), userId)) {
            return ApiResponse.authority("当前用户不存在这个数据库组");
        }

        int count = dataBaseGroupRepository.containsByUserIdAndDataBaseGroupName(userId, databaseGroupName);
        if (count > 0) {
            return ApiResponse.authority("数据库组名称已经存在");
        }

        databaseGroup.setDatabaseGroupName(databaseGroupName);
        databaseGroup.setVersion(new BigDecimal(version));

        // 乐观锁机制,避免重复提交修改请求造成的数据混乱
        int status = dataBaseGroupRepository.updateByVersion(databaseGroup);

        if (status > 0) {
            // 因为 version 已经改变,并且数据可能存在并发修改问题. 我们需要拿到最新版本
            return ApiResponse.success(dataBaseGroupRepository.selectPrimary(databaseGroupId));
        }

        return ApiResponse.server("修改数据库组名称失败");
    }

    /**
     * 删除指定用户的数据库组
     *
     * @param userId          用户ID
     * @param databaseGroupId 数据库组ID
     * @param validationCode  数据库组名称效验码
     * @return
     */
    public ApiResponse deleteDatabaseGroup(String userId, String databaseGroupId, String validationCode) {
        DatabaseGroup databaseGroup = dataBaseGroupRepository.selectPrimary(databaseGroupId);

        if (databaseGroup == null) {
            return ApiResponse.error("数据库组不存在");
        }

        // 涉及到横向越权
        if (!StringUtils.equals(databaseGroup.getUserId(), userId)) {
            return ApiResponse.authority("当前用户不存在这个数据库组");
        }

        if (!databaseGroup.getDatabaseGroupName().equals(validationCode)) {
            return ApiResponse.authority("效验码错误");
        }

        int status = dataBaseGroupRepository.deletePrimary(databaseGroupId);
        if (status > 0) {
            return ApiResponse.success("删除数据库组成功");
        }

        return ApiResponse.server("删除数据库组失败");
    }


    /**
     * 获取当前用户的数据库组列表
     *
     * @param userId 用户ID
     * @return
     */
    public List<DatabaseGroup> getDataBaseGroupList(String userId) {
        return dataBaseGroupRepository.selectByUserId(userId);
    }


}
