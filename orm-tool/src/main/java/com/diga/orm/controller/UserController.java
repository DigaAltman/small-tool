package com.diga.orm.controller;

import com.diga.orm.bo.UserBO;
import com.diga.orm.common.ApiResponse;
import com.diga.orm.common.WorkCommon;
import com.diga.orm.pojo.work.User;
import com.diga.orm.service.impl.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "用户接口", tags = {"用户接口"})
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    @ApiOperation(value = "用户登录", httpMethod = "POST")
    public ApiResponse login(@Validated(UserBO.Login.class) @RequestBody UserBO userBO, HttpSession session) {
        ApiResponse response = userService.login(userBO.getUsername(), userBO.getPassword());
        if (response.statusSuccess()) {
            Object user = response.getData();
            session.setAttribute(WorkCommon.CURRENT_USER, user);
        }

        return ApiResponse.authority("输入的两次密码不一致");
    }


    @GetMapping("/message")
    @ApiOperation(value = "获取当前用户信息", httpMethod = "GET")
    public ApiResponse getCurrentUser(User user) {
        if (user != null) {
            return ApiResponse.success(user);
        }

        return ApiResponse.login("当前用户未登录,请先登录");
    }


    @GetMapping("/exit")
    @ApiOperation(value = "退出登录", httpMethod = "GET")
    public ApiResponse exit(HttpSession session, User user) {
        if (user != null) {
            session.removeAttribute(WorkCommon.CURRENT_USER);
            return ApiResponse.success("用户退出成功");
        }

        return ApiResponse.login("用户未登录,无法退出");
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册", httpMethod = "POST")
    public ApiResponse register(@Validated(UserBO.Register.class) @RequestBody UserBO userBO) {
        if (!StringUtils.equals(userBO.getPassword(), userBO.getConfirmPassword())) {
            return ApiResponse.authority("输入的两次密码不一致");
        }
        return userService.register(userBO);
    }


    // 根据用户名重置密码
    @GetMapping("/forgetByUsername")
    @ApiOperation(value = "根据用户名称重置密码", httpMethod = "GET")
    @Valid
    public ApiResponse forgetByUsername(
            HttpSession session,

            @ApiParam("用户名称")
            @NotEmpty(message = "用户名不能为空")
            @Range(min = 6, max = 12, message = "用户名有效长度 [6-12] 位") String username) {
        return userService.forgetByUsername(username, session);
    }

    // 根据邮箱重置密码
    @GetMapping("/forgetByEmail")
    @ApiOperation(value = "根据邮箱重置密码", httpMethod = "GET")
    @Valid
    public ApiResponse forgetByEmail(
            HttpSession session,

            @ApiParam("邮箱名称")
            @NotEmpty(message = "邮箱不能为空")
            @Email(message = "邮箱格式不正确")
            @Range(min = 0, max = 50, message = "邮箱邮箱长度位 [0-50] 位") String email) {
        return userService.forgetByEmail(email, session);
    }


    // 重置密码
    @PostMapping("/reset")
    @ApiOperation(value = "重置密码", httpMethod = "POST")
    public ApiResponse reset(HttpSession session,
                             @ApiParam("密码")
                             @NotEmpty(message = "密码不能为空")
                             @Range(min = 6, max = 12, message = "密码有效长度为 [6-12] 位") String password,

                             @ApiParam("二次输入密码")
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
