/*
 * Created by Long Duping
 * Date 2018/12/5 15:40
 */
package com.hang.api;

import com.alibaba.fastjson.JSONObject;
import com.hang.service.SessionService;
import com.hang.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author test
 */
@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "miniProgramUserService")
    private UserService miniProgramUserService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/bind")
    public String bind() {
        return "暂未实现";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String code, @RequestParam String rawData) {
        log.info("appPlatform: {}", request.getHeader("appPlatform"));
        log.info("appVersion: {}", request.getHeader("appVersion"));
        log.info("code: {}", code);
        log.info("rawData: {}", rawData);
        if (StringUtils.isEmpty(code)) {
            JSONObject returnJson = new JSONObject();
            returnJson.put("errcode", 10000);
            returnJson.put("errmsg", "code is empty");
            log.error(returnJson.toString());
            return returnJson.toString();
        }
        return miniProgramUserService.login(code, rawData);
    }

    @GetMapping("/getUserInfo")
    public String getUserInfo(String sessionId) throws IOException {
        log.info("sessionId: " + request.getHeader("sessionId"));
        log.info("appPlatform: " + request.getHeader("appPlatform"));
        log.info("appVersion: " + request.getHeader("appVersion"));

        if (Strings.isEmpty(sessionId)) {
            sessionId = request.getHeader("sessionId");
        }
        JSONObject returnJson = new JSONObject();
        JSONObject sessionJson = sessionService.getSessionInfo(sessionId);
        int errcode = sessionJson.getIntValue("errcode");
        if (0 != errcode) {
            returnJson.put("errcode", errcode);
            returnJson.put("errmsg", sessionJson.getString("errmsg"));
            return returnJson.toString();
        }

        return sessionJson.toString();
    }
}
