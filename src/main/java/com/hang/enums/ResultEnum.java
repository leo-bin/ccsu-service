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
    ;

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
