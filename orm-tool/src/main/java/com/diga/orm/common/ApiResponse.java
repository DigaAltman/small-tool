package com.diga.orm.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    /**
     * 返回结果状态码
     */
    private int status;

    /**
     * 返回结果信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 请求成功
     * @return
     */
    public boolean statusSuccess() {
        return this.status == StatusEnum.SUCCESS.getStatus();
    }

    private ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private ApiResponse(int status, String message) {
        this(status, message, null);
    }


    /**
     * 不包含空内容的成功返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse(StatusEnum.SUCCESS.status, StatusEnum.SUCCESS.message);
    }

    /**
     * 包含空内容的成功返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse(StatusEnum.SUCCESS.status, StatusEnum.SUCCESS.message, data);
    }

    /**
     * 包含消息的空内容成功返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse(StatusEnum.SUCCESS.status, message, null);
    }

    /**
     * 不包含内容的服务器错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> server() {
        return new ApiResponse(StatusEnum.SERVER.status, StatusEnum.SERVER.message);
    }

    /**
     * 不包含内容的服务器错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> server(String message) {
        return new ApiResponse(StatusEnum.SERVER.status, message);
    }


    /**
     * 不包含内容的请求错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> error() {
        return new ApiResponse(StatusEnum.ERROR.status, StatusEnum.ERROR.message);
    }

    /**
     * 包含消息提示的请求错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse(StatusEnum.ERROR.status, message);
    }

    /**
     * 包含消息提示的验证错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> validation(String message) {
        return new ApiResponse(StatusEnum.VALIDATION.status, message);
    }

    /**
     * 不包含消息提示的验证错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> validation() {
        return new ApiResponse(StatusEnum.VALIDATION.status, StatusEnum.VALIDATION.message);
    }

    /**
     * 不包含消息提示的登陆错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> login() {
        return new ApiResponse(StatusEnum.NEED_LOGIN.status, StatusEnum.NEED_LOGIN.message);
    }

    /**
     * 包含消息提示的登陆错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> login(String message) {
        return new ApiResponse(StatusEnum.NEED_LOGIN.status, message);
    }

    /**
     * 包含消息提示的令牌错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> token(String message) {
        return new ApiResponse(StatusEnum.TOKEN.status, message);
    }

    /**
     * 不包含消息提示的令牌错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> token() {
        return new ApiResponse(StatusEnum.TOKEN.status, StatusEnum.TOKEN.message);
    }

    /**
     * 包含消息提示的权限错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> authority(String message) {
        return new ApiResponse(StatusEnum.AUTHORITY.status, message);
    }

    /**
     * 不包含消息提示的权限错误返回
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> authority() {
        return new ApiResponse(StatusEnum.AUTHORITY.status, StatusEnum.AUTHORITY.message);
    }


    /**
     * 自定义返回结果
     *
     * @param status  状态码
     * @param message 消息
     * @param data    数据
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> other(int status, String message, T data) {
        return new ApiResponse(status, message, data);
    }


    /**
     * 状态码枚举类
     */
    @Getter
    public enum StatusEnum {
        SUCCESS(200, "请求成功"),
        SERVER(500, "服务器内部错误"),
        VALIDATION(501, "验证错误"),
        NEED_LOGIN(502, "未登陆错误"),
        TOKEN(503, "token错误"),
        AUTHORITY(504, "权限错误"),
        ERROR(404, "请求出错");

        private int status;
        private String message;

        StatusEnum(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
