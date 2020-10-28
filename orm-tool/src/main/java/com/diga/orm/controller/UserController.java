package com.diga.orm.controller;

import com.diga.orm.bo.UserBO;
import com.diga.orm.common.ApiResponse;
import com.diga.orm.common.WorkCommon;
import com.diga.orm.pojo.work.User;
import com.diga.orm.service.impl.UserService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ApiResponse login(@Validated(UserBO.Login.class) @RequestBody UserBO userBO, HttpSession session) {
        ApiResponse response = userService.login(userBO.getUsername(), userBO.getPassword());
        if (response.statusSuccess()) {
            Object user = response.getData();
            session.setAttribute(WorkCommon.CURRENT_USER, user);
        }

        return ApiResponse.authority("输入的两次密码不一致");
    }


    @GetMapping("/message")
    public ApiResponse getCurrentUser(User user) {
        if (user != null) {
            return ApiResponse.success(user);
        }

        return ApiResponse.login("当前用户未登录,请先登录");
    }


    @GetMapping("/exit")
    public ApiResponse exit(HttpSession session, User user) {
        if (user != null) {
            session.removeAttribute(WorkCommon.CURRENT_USER);
            return ApiResponse.success("用户退出成功");
        }

        return ApiResponse.login("用户未登录,无法退出");
    }

    @PostMapping("/register")
    public ApiResponse register(@Validated(UserBO.Register.class) @RequestBody UserBO userBO) {
        if (!StringUtils.equals(userBO.getPassword(), userBO.getConfirmPassword())) {
            return ApiResponse.authority("输入的两次密码不一致");
        }
        return userService.register(userBO);
    }


    // 根据用户名重置密码
    @GetMapping("/forgetByUsername")
    @Valid
    public ApiResponse forgetByUsername(
            HttpSession session,
            @NotEmpty(message = "用户名不能为空")
            @Range(min = 6, max = 12, message = "用户名有效长度 [6-12] 位") String username) {
        return userService.forgetByUsername(username, session);
    }

    // 根据邮箱重置密码
    @GetMapping("/forgetByEmail")
    @Valid
    public ApiResponse forgetByEmail(
            HttpSession session,
            @NotEmpty(message = "邮箱不能为空")
            @Email(message = "邮箱格式不正确")
            @Range(min = 0, max = 50, message = "邮箱邮箱长度位 [0-50] 位") String email) {
        return userService.forgetByEmail(email, session);
    }


    // 重置密码
    @PostMapping("/reset")
    public ApiResponse reset(HttpSession session,
                             @NotEmpty(message = "密码不能为空")
                             @Range(min = 6, max = 12, message = "密码有效长度为 [6-12] 位") String password,

                             @NotEmpty(message = "确认密码不能为空")
                             @Range(min = 6, max = 12, message = "密码有效长度为 [6-12] 位") String confirmPassword) {
        Object token = session.getAttribute("token");
        if (token == null) {
            return ApiResponse.token("没有重置密码的权限授权码");
        }

        if (StringUtils.isBlank(password)) {
            return ApiResponse.validation("密码不能为空");
        }

        if (!StringUtils.equals(password, confirmPassword)) {
            return ApiResponse.validation("两次密码不一致");
        }

        return userService.reset(token.toString(), password);
    }

}
