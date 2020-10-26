package com.diga.orm.service.impl;

import com.diga.generic.utils.EncryptionUtil;
import com.diga.orm.bo.UserBO;
import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.work.User;
import com.diga.orm.repository.impl.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 用户登陆
     *
     * @param username 用户名称
     * @param password 用户密码
     * @return
     */
    public ApiResponse login(String username, String password) {
        User user = userRepository.selectByUserName(username);
        if (user == null) {
            return ApiResponse.login("用户不存在");
        }
        if (!StringUtils.equals(EncryptionUtil.md5(password), user.getPassword())) {
            return ApiResponse.login("密码不正确");
        }
        user.setPassword(null);
        return ApiResponse.success(user);
    }

    /**
     * 用户注册
     *
     * @param userBO
     * @return
     */
    public ApiResponse register(UserBO userBO) {
        User dataUser = userRepository.selectByUserName(userBO.getUsername());
        if (dataUser != null) {
            return ApiResponse.login("用户名称已经被使用");
        }

        User user = userBO.toUser();
        user.setPassword(EncryptionUtil.md5(user.getPassword()));
        user.setUserId(UUID.randomUUID().toString());
        user.setVersion(new BigDecimal(1));

        // 默认昵称为ID
        if (StringUtils.isBlank(user.getRealname())) {
            user.setRealname(user.getUserId());
        }

        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        int status = userRepository.insert(user);
        if (status > 0) {
            return ApiResponse.success();
        }
        return ApiResponse.server("用户注册失败");
    }


}
