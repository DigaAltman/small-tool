package com.diga.orm.bo;

import com.diga.orm.pojo.work.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@ApiModel(value = "用户对象BO", description = "前端传入的用户注册的信息转换的实体")
@Data
public class UserBO implements Serializable {
    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    @NotEmpty(message = "用户名不能为空", groups = {Login.class, Register.class})
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("用户密码")
    @NotEmpty(message = "密码不能为空", groups = {Login.class, Register.class})
    @Length(min = 6, message = "密码长度不能低于6位", groups = {Login.class, Register.class})
    private String password;

    /**
     * 确认密码
     */
    @ApiModelProperty("二次输入的密码")
    @NotEmpty(message = "密码不能为空", groups = {Register.class})
    @Length(min = 6, message = "密码长度不能低于6位", groups = {Register.class})
    private String confirmPassword;

    /**
     * 绑定邮箱
     */
    @ApiModelProperty("用户绑定的邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 用户的昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;


    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealname(nickname);
        user.setEmailAddress(email);
        return user;
    }

    // 登录
    public interface Login {
    }

    // 注册
    public interface Register {
    }

}
