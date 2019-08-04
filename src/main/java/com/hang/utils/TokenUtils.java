/*
 * Created by Long Duping
 * Date 2018/12/5 12:41
 */
package com.hang.utils;

import java.util.UUID;

/**
 * @apiNote 随机令牌生成工具类
 */
public final class TokenUtils {

    private final static String PREFIX = "CCSU.MICRO.PLATFORM.SESSION.";

    public static synchronized String getToken() {
        return PREFIX + UUID.randomUUID().toString().toUpperCase();
    }

}
