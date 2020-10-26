package com.diga.orm.controller;

import com.diga.orm.bo.UserBO;
import com.diga.orm.common.ApiResponse;
import com.diga.orm.common.WorkCommon;
import com.diga.orm.service.impl.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/tab")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ApiResponse login(@RequestParam(name = "user") @RequestBody @Validated UserBO userBO, HttpSession session) {

        if (StringUtils.equals(userBO.getPassword(), userBO.getConfirmPassword())) {
            ApiResponse response = userService.login(userBO.getUsername(), userBO.getPassword());
            if (response.statusSuccess()) {
                Object user = response.getData();
                session.setAttribute(WorkCommon.CURRENT_USER, user);
            }
        }
        return ApiResponse.authority("输入的两次密码不一致");
    }


    @GetMapping("/message")
    public ApiResponse getCurrentUser(HttpSession session) {
        Object user = session.getAttribute(WorkCommon.CURRENT_USER);

        if (user != null) {
            return ApiResponse.success(user);
        }

        return ApiResponse.login("当前用户未登录,请先登录");
    }


    @GetMapping("/exit")
    public ApiResponse exit(HttpSession session) {
        Object user = session.getAttribute(WorkCommon.CURRENT_USER);

        if (user != null) {
            session.removeAttribute(WorkCommon.CURRENT_USER);
            return ApiResponse.success();
        }

        return ApiResponse.login();
    }

    @PostMapping("/register")
    public ApiResponse register(UserBO userBO) {
        if (!StringUtils.equals(userBO.getPassword(), userBO.getConfirmPassword())) {
            return ApiResponse.authority("输入的两次密码不一致");
        }
        return userService.register(userBO);
    }


    // 根据用户名重置密码
    @GetMapping("/forgetByUsername")
    @Validated
    public ApiResponse forgetByUsername(@NotEmpty(message = "用户名不能为空") String username) {
        return userService.forgetByUsername(username);
    }

    // 根据邮箱重置密码
    @GetMapping("/forgetByEmail")
    @Validated
    public ApiResponse forgetByEmail(@NotEmpty(message = "邮箱不能为空") String email) {
        return userService.forgetByEmail(email);
    }
}
