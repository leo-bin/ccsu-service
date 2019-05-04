/*
 * Created by Long Duping
 * Date 2018/12/5 12:35
 */
package com.hang.api;

import com.hang.aop.StatisticsTime;
import com.hang.pojo.vo.BaseRes;
import com.hang.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author test
 */
@Api("回话服务接口")
@RestController
@RequestMapping("/session")
public class SessionController {
    private static final Logger log = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    private SessionService sessionService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 开启一个新的会话
     *
     * @param data
     * @return 新的会话ID
     */
    @StatisticsTime("newSession")
    @ApiOperation("新建立一个会话")
    @RequestMapping("/newSession")
    public String newSession(@RequestParam String data) {
        return sessionService.newSession(data).toJSONString();
    }

    /**
     * 根据会话ID获取会话信息
     *
     * @param sessionId
     * @return 会话信息
     */
    @StatisticsTime("getSessionInfo")
    @ApiOperation("获取会话信息")
    @RequestMapping(value = "/getSessionInfo", method = RequestMethod.GET)
    public BaseRes getSessionInfo(@RequestBody String json, String sessionId) {
        log.info("sessionId: " + request.getHeader("sessionId"));
        log.info("appPlatform: " + request.getHeader("appPlatform"));
        log.info("appVersion: " + request.getHeader("appVersion"));
        log.info("body", json);
        if (Strings.isEmpty(sessionId)) {
            sessionId = request.getHeader("sessionId");
        }
        return sessionService.getSessionInfo(sessionId);
    }
}
