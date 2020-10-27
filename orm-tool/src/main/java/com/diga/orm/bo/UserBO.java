package com.diga.orm.bo;

import com.diga.orm.pojo.work.User;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class UserBO implements Serializable {
    /**
     * 用户名称
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Range(min = 6, message = "密码长度不能低于6位")
    private String password;

    /**
     * 确认密码
     */
    @Range(min = 6, message = "密码长度不能低于6位")
    private String confirmPassword;

    /**
     * 绑定邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 用户的昵称
     */
    private String nickname;


    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealname(nickname);
        user.setEmailAddress(email);
        return user;
    }
}
