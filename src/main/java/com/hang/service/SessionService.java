/*
 * Created by Long Duping
 * Date 2019-02-15 16:16
 */
package com.hang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hang.pojo.data.UserInfoDO;
import com.hang.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author test
 */
@Service
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    /**
     * 会话过期时间（默认 28800 秒，八个小时）
     */
    @Value("${expireIn: 28800}")
    private int expiredIn;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public JSONObject newSession(String data) {
        JSONObject returnJson = new JSONObject();
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String sessionId = TokenUtils.getToken();
        log.info("redis set {} = {}", sessionId, data);
        ops.set(sessionId, data == null ? "null" : data, expiredIn, TimeUnit.SECONDS);
        returnJson.put("code", 0);
        returnJson.put("msg", "success");
        returnJson.put("sessionId", sessionId);
        returnJson.put("expired_in", expiredIn);
        return returnJson;
    }

    public UserInfoDO getSessionInfo(String sessionId) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String sessionInfo = ops.get(sessionId);
        UserInfoDO userInfoDO = JSON.parseObject(sessionInfo, UserInfoDO.class);
        log.info("redis sessionId : {}, sessionInfo : {}", sessionId, sessionInfo);
        redisTemplate.expire(sessionId, expiredIn, TimeUnit.SECONDS);
        return userInfoDO;
    }
}