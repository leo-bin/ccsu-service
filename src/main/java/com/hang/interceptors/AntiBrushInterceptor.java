package com.hang.interceptors;

import com.alibaba.fastjson.JSON;
import com.hang.annotation.AccessLimit;
import com.hang.enums.ResultEnum;
import com.hang.utils.RedisUtil;
import com.hang.utils.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author leo-bin
 * @date 2019/12/23 18:41
 * @apiNote 自定义接口防刷拦截器
 */
@Slf4j
@Component
public class AntiBrushInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = (Logger) LoggerFactory.getLogger(AntiBrushInterceptor.class);

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求的对象是否是方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法中的注解，判断方式是否加了注解
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int second = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean login = accessLimit.needLogin();
            //获取请求的路径
            String key = request.getRequestURI();
            //需要判断是否登录
            if (login) {
                String sessionId = request.getHeader("sessionId");
                if (sessionId != null) {
                    //根据sessionId和请求路径拼接成一个唯一的key
                    key = sessionId + "|" + key;
                }
            }
            //从redis中获取用户的登录次数
            Integer counts = (Integer) redisUtil.get(key);
            if (counts != null) {
                if (counts < maxCount) {
                    //设置key-value的过期时间，并且将value+1
                    redisUtil.incr(key, second);
                } else {
                    //超出访问次数
                    String mess = "当前访问次数:" + counts + "；限制的最大访问次数：" + maxCount;
                    logger.info("超出限制范围了：{}", mess);
                    //返回错误信息
                    render(response);
                    return false;
                }
            } else {
                //第一次访问，默认的递增因子设置为 "1"
                redisUtil.set(key, 1);
            }
        }
        return true;
    }

    private void render(HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream outputStream = response.getOutputStream();
        //将错误信息写出
        String str = JSON.toJSONString(RespUtil.error(ResultEnum.ACCESS_LIMIT_REACHED));
        outputStream.write(str.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
