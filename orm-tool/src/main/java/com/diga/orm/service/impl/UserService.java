package com.diga.orm.service.impl;

import com.diga.generic.utils.EncryptionUtil;
import com.diga.generic.utils.JsonUtils;
import com.diga.orm.bo.UserBO;
import com.diga.orm.common.ApiResponse;
import com.diga.orm.pojo.work.User;
import com.diga.orm.repository.UserRepository;
import com.diga.orm.vo.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

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

        User emailUser = userRepository.selectByEmail(userBO.getEmail());
        if (emailUser != null) {
            return ApiResponse.login("邮箱地址已经被使用了");
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


    /**
     * 根据用户名称重置密码
     *
     * @param username
     */
    public ApiResponse forgetByUsername(String username, HttpSession session) {
        User user = userRepository.selectByUserName(username);
        if (user == null) {
            return ApiResponse.login("用户不存在");
        }

        String emailAddress = user.getEmailAddress();
        if (StringUtils.isBlank(emailAddress)) {
            return ApiResponse.login("用户没有绑定邮箱,无法找回密码");
        }

        // 将这个用户名称注入进去
        Token token = new Token("username", username, System.currentTimeMillis() + 600);
        String json = JsonUtils.stringify(token);
        String tokenJson = EncryptionUtil.encryption(json);

        // TODO 保存 token 标识到会话中,后续修改为 Redis Cache
        session.setAttribute("username", username);

        mailService.sendHtmlMail("1450160028@qq.com", "STool [忘记密码] ", "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\"><title>STool</title></head><body><div id=\"app\"><h4>邮件说明</h4><div style=\"color: orangered;\">这是一份重置密码的提示邮件, 当用户忘记密码时, 可通过此邮件挑战到重置密码页面.请不要将此页面分享或转发给其他人, 否则你的账号可能会十分危险.</div><h3>跳转到重置密码网页</h3><h4><a href=\"http://localhost/user/reset?token=" + tokenJson + "\">点我跳转</a></h4><h3>如果无法跳转,请复制下面的链接地址. 到浏览器打开</h3><h4>http://localhost/user/reset?token=" + tokenJson + "</h4></div></body></html>");
        return ApiResponse.success("邮件发送成功");
    }


    /**
     * 根据用户名或邮箱重置密码
     *
     * @param session
     * @param token
     * @param password
     * @return
     */
    public ApiResponse reset(HttpSession session, String token, String password) {
        Token tokenBean;
        try {
            String json = EncryptionUtil.decrypt(token);
            tokenBean = JsonUtils.parse(json, Token.class);
        } catch (Exception e) {
            return ApiResponse.validation("授权码格式不正确");
        }

        if (tokenBean.getTermOfValidity() < System.currentTimeMillis()) {
            return ApiResponse.token("token已经失效了");
        }
        User user = null;

        if (StringUtils.equals(tokenBean.getTitle(), "username")) {

            Object username = session.getAttribute("username");
            if (username == null || !StringUtils.equals(username.toString(), tokenBean.getBody())) {
                return ApiResponse.validation("效验身份信息失败...");
            }

            user = userRepository.selectByUserName(tokenBean.getBody());
        } else if (StringUtils.equals(tokenBean.getTitle(), "email")) {

            Object email = session.getAttribute("email");
            if (email == null || !StringUtils.equals(email.toString(), tokenBean.getBody())) {
                return ApiResponse.validation("效验身份信息失败...");
            }

            user = userRepository.selectByEmail(tokenBean.getBody());
        }

        if (user == null) {
            return ApiResponse.authority("用户已经找不到了");
        }

        user.setPassword(EncryptionUtil.md5(password));
        int status = userRepository.update(user);
        if (status > 0) {
            return ApiResponse.success("重置密码成功,请尝试重新登录");
        }

        return ApiResponse.error("密码重置失败");
    }


    /**
     * 根据用户绑定的邮箱重置密码
     *
     * @param email
     */
    public ApiResponse forgetByEmail(String email, HttpSession session) {
        User user = userRepository.selectByEmail(email);
        if (user == null) {
            return ApiResponse.login("用户不存在");
        }

        // 将这个用户名称注入进去
        Token token = new Token("email", email, System.currentTimeMillis() + 600);
        String json = JsonUtils.stringify(token);
        String tokenJson = EncryptionUtil.encryption(json);

        // TODO 保存 token 标识到会话中,后续修改为 Redis Cache
        session.setAttribute("email", email);

        mailService.sendHtmlMail("1450160028@qq.com", "STool [忘记密码] ", "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\"><title>STool</title></head><body><div id=\"app\"><h4>邮件说明</h4><div style=\"color: orangered;\">这是一份重置密码的提示邮件, 当用户忘记密码时, 可通过此邮件挑战到重置密码页面.请不要将此页面分享或转发给其他人, 否则你的账号可能会十分危险.</div><h3>跳转到重置密码网页</h3><h4><a href=\"http://localhost/user/reset?token=" + tokenJson + "\">点我跳转</a></h4><h3>如果无法跳转,请复制下面的链接地址. 到浏览器打开</h3><h4>http://localhost/user/reset?token=" + tokenJson + "</h4></div></body></html>");
        return ApiResponse.success();
    }

}
