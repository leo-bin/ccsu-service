package com.hang.exceptions;

import com.hang.enums.ResultEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Created by tomoya at 2018/3/15
 */
public class ApiAssert extends Assert {

    public static void checkOpenId(String openId) {
        if (StringUtils.isEmpty(openId)) {
            throw new ApiException(ResultEnum.CAN_NOT_GET_OPEN_ID);
        }
    }

    public static void nonEqualString(String str1, String str2, String message) {
        if (!StringUtils.equals(str1, str2)) {
            throw new ApiException(-1, message);
        }
    }

    public static void nonEqualInteger(Integer i1, Integer i2, String message) {
        if (!Objects.equals(i1, i2)) {
            throw new ApiException(-1, message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new ApiException(-1, message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new ApiException(-1, message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ApiException(-1, message);
        }
    }

    public static void notTrue(boolean expression, String message) {
        if (expression) {
            throw new ApiException(-1, message);
        }
    }

    public static void isEmpty(String txt, String message) {
        if (!StringUtils.isEmpty(txt)) {
            throw new ApiException(-1, message);
        }
    }

    public static void notEmpty(String txt, String message) {
        if (StringUtils.isEmpty(txt)) {
            throw new ApiException(-1, message);
        }
    }
}
