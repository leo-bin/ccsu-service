/*
 * Created by Long Duping
 * Date 2018/12/5 14:35
 */
package com.hang.service;

import com.alibaba.fastjson.JSONObject;
import com.hang.constant.WxConstant;
import com.hang.dao.UserInfoDAO;
import com.hang.exceptions.ApiException;
import com.hang.pojo.data.StudentDO;
import com.hang.pojo.data.UserInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;

/**
 * @author test
 */
@Slf4j
@Service
public class UserService {

    @Value("${wx.appId: wxba259c9cc25c8a20}")
    private String appId;

    @Value("${wx.appSecret: c911996088c1946f02d1af55d452a539}")
    private String appSecret;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserInfoDAO userInfoDAO;

    @Autowired
    private StudentService studentService;

    public UserInfoDO getUserInfoByOpenId(String openId) {
        return userInfoDAO.selectByOpenId(openId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bind(String openId, String jwcAccount) {
        updateJwcAccount(openId, jwcAccount);

        UserInfoDO userInfoDO = userInfoDAO.selectByOpenId(openId);
        StudentDO studentDO = new StudentDO();
        studentDO.setJwcAccount(userInfoDO.getJwcAccount());
        studentDO.setNickName(userInfoDO.getNickName());
        studentDO.setGrade(userInfoDO.getJwcAccount().substring(1, 5));
        studentDO.setOpenId(userInfoDO.getOpenId());
        studentService.saveStudentInfo(studentDO);
    }

    @Transactional(rollbackFor = ApiException.class)
    public void updateJwcAccount(String openId, String jwcAccount) {
        int i = userInfoDAO.updateJwcAccount(openId, jwcAccount.toUpperCase());
        if (i != 1) {
            throw new ApiException(-1, "绑定失败");
        }
    }

    public String login(String code, String rawData) {
        JSONObject returnJson = new JSONObject();
        // 用code 去微信服务器拿 openId 和 session_key
        JSONObject openIdAndSessionKey = code2session(code);
        int errcode = openIdAndSessionKey.getIntValue("errcode");
        if (errcode != 0) {
            returnJson.put("errcode", 10011);
            returnJson.put("errmsg", openIdAndSessionKey.getString("errmsg"));
            log.error(returnJson.toString());
            return returnJson.toJSONString();
        }
        // 根据openId 查询用户信息
        String openId = openIdAndSessionKey.getString("openId");
        log.debug("openid: {}", openId);

        UserInfoDO userInfo = new UserInfoDO();
        userInfo.setOpenId(openId);
        if (!Strings.isEmpty(rawData)) {

            try {
                JSONObject json = JSONObject.parseObject(rawData);
                log.debug("rawData: {}", rawData);
                userInfo.setNickName(json.getString("nickName"));
                userInfo.setAvatarUrl(json.getString("avatarUrl"));
                userInfo.setCity(json.getString("city"));
                userInfo.setCountry(json.getString("country"));
                userInfo.setGender(json.getInteger("gender"));
                userInfo.setProvince(json.getString("province"));
                userInfo.setRoleId(0);
                userInfo.setStuNumber("");
                userInfo.setRealName("");
                userInfo.setCreateTime(new Date(System.currentTimeMillis()));
                userInfo.setLastLoginTime(new Date(System.currentTimeMillis()));
                if (userInfoDAO.isExist(openId)) {
                    userInfoDAO.updateLastLoginTime(openId);
                } else {
                    userInfoDAO.insert(userInfo);
                }
            } catch (Exception e) {
                returnJson.put("errcode", 10000);
                returnJson.put("errmsg", "error:" + e.getMessage());
                return returnJson.toString();
            }
        }
        // 将用户信息写入会话缓存
        JSONObject sessionJson = sessionService.newSession(JSONObject.toJSONString(userInfo));
        errcode = sessionJson.getIntValue("errcode");
        if (0 != errcode) {
            returnJson.put("errcode", errcode);
            returnJson.put("errmsg", sessionJson.getString("errmsg"));
            return returnJson.toString();
        }
        returnJson.put("errcode", 0);
        returnJson.put("errmsg", "success");
        returnJson.put("userInfo", userInfo);
        returnJson.put("sessionId", sessionJson.getString("sessionId"));
        return returnJson.toString();
    }

    private JSONObject code2session(String code) {
        JSONObject returnJson = new JSONObject();
        log.debug("appid={},appSecret={}", appId, appSecret);
        URI uri = UriComponentsBuilder.fromUriString(WxConstant.WxApi.CODE_TO_SESSION)
                .build()
                .expand(appId, appSecret, code)
                .encode()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity(uri, String.class);
        if (HttpStatus.OK == entity.getStatusCode()) {
            JSONObject res = JSONObject.parseObject(entity.getBody());
            int errcode = res.getIntValue("errcode");
            returnJson.put("errcode", errcode);
            if (0 == errcode) {
                // 请求成功
                returnJson.put("openId", res.getString("openid"));
                returnJson.put("sessionKey", res.getString("session_key"));
            } else {
                returnJson.put("errmsg", res.getString("errmsg"));
            }
            return returnJson;
        }
        returnJson.put("errcode", entity.getStatusCodeValue());
        returnJson.put("errmsg", "http connect " + uri.toString() + " failed");
        return returnJson;
    }

}
