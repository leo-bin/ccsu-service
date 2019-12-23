package com.hang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leo-bin
 * @date 2019/12/23 18:33
 * @apiNote 自定义防刷接口注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    /**
     * 限制访问时间，单位为秒
     */
    int seconds();

    /**
     * 在访问时间内的最大访问次数
     */
    int maxCount();

    /**
     * 是否需要判断登陆
     */
    boolean needLogin() default true;
}
