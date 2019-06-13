package com.hang.enums;

import lombok.Getter;

/**
 * @author hangs.zhang
 * @date 2018/12/10
 * *****************
 * function:
 * api 返回码
 */
@Getter
public enum ResultEnum {
    /**
     * 成功
     */
    SUCCESS(0, "success"),
    /**
     * 参数错误
     */
    PARAM_ERROR(-10005, "param error"),
    /**
     * 服务器内部异常
     */
    SERVER_INNER_ERROR(-10006, "server inner error"),

    /**
     * HTTP请求头错误
     */
    HEADERS_ERROR(-10007, "header error"),

    /**
     * 无法获取open id
     */
    CAN_NOT_GET_OPEN_ID(-10008, "can not get open id"),

    /**
     * 无法获取用户信息
     */
    CAN_NOT_GET_USER_INFO(-10009, "can not get user info"),

    /**
     * 学号信息未绑定
     */
    JWC_ACCOUNT_NOT_BIND(-10010, "jwd account not bind"),

    /**
     * 导师姓名为空
     */
    ADVISERNAME_IS_NULL(-10011,"adviser name is null"),


    /**
     * 导师不存在
     */
    ADVISER_NOT_EXIT(-10012,"adviser is not exit");

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 具体信息
     */
    private String msg;


    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
