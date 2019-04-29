package com.hang.exceptions;

import com.hang.enums.ResultEnum;
import lombok.Getter;

/**
 * @author hangs.zhang
 * @date 2018/11/20
 * *****************
 * function:
 */
@Getter
public class ApiException extends RuntimeException {

    private Integer code;

    public ApiException(ResultEnum resultEnum) {
        this(resultEnum.getCode(), resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public ApiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException() {
        super();
    }

}
