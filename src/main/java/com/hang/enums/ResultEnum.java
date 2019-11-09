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
     * 账号未绑定
     */
    ACCOUNT_NOT_BIND(-10010, "jwc account not bind"),

    /**
     * 导师姓名为空
     */
    ADVISERNAME_IS_NULL(-10011, "adviser name is null"),


    /**
     * 导师不存在
     */
    ADVISER_NOT_EXIT(-10012, "adviser is not exit"),


    /**
     * 话题已经存在
     */
    TOPIC_IS_EXIT(-10013, "topic is exit"),


    /**
     * 团队不存在
     */
    TEAM_NOT_EXIT(-10014, "team is not exit"),

    /**
     * 权限错误
     */
    AUTHORIZE_ERROR(-10015, "you have no authorize"),

    /**
     * 学号密码错误
     */
    JWC_ACCOUNT_OR_CODE_ERROR(-10016, "student number or code is wrong"),

    /**
     * 教工号格式错误
     */
    STAFF_NUMBER_ERROR(-10017, "teacher number is wrong"),

    /**
     * 网络错误
     */
    NETWORK_ERROR(-10018,"your network is blocked unexpected"),

    /**
     * 课表更新失败
     */
    COURSE_UPDATE_ERROR(-10019,"course update error");


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
