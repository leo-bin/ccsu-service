/*
 * Created by Long Duping
 * Date 2018/12/5 15:40
 */
package com.hang.api;

import com.alibaba.fastjson.JSONObject;
import com.hang.annotation.OpenId;
import com.hang.aop.StatisticsTime;
import com.hang.enums.ResultEnum;
import com.hang.exceptions.ApiAssert;
import com.hang.pojo.data.AdviserDO;
import com.hang.pojo.data.StudentDO;
import com.hang.pojo.data.UserInfoDO;
import com.hang.pojo.vo.BaseRes;
import com.hang.service.AdviserService;
import com.hang.service.SessionService;
import com.hang.service.StudentService;
import com.hang.service.UserService;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author test
 */
@Api("用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdviserService adviserService;

    @StatisticsTime("bind")
    @ApiOperation("绑定学号信息，openId参数不用传")
    @PostMapping("/bind")
    public BaseRes bind(@OpenId String openId, @RequestParam String jwcAccount) {
        log.info("jwcAccount:{}, openId:{}", jwcAccount, openId);
        userService.bind(openId, jwcAccount);
        return RespUtil.success();
    }

    @StatisticsTime("login")
    @ApiOperation("登录，获取sessionId")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String code, @RequestParam String rawData) {
        log.info("appPlatform: {}", request.getHeader("appPlatform"));
        log.info("appVersion: {}", request.getHeader("appVersion"));
        log.info("code: {}", code);
        log.info("rawData: {}", rawData);
        if (StringUtils.isEmpty(code)) {
            JSONObject returnJson = new JSONObject();
            returnJson.put("code", 10000);
            returnJson.put("msg", "code is empty");
            log.error(returnJson.toString());
            return returnJson.toString();
        }
        return userService.login(code, rawData);
    }

    @StatisticsTime("getUserInfo")
    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    public BaseRes getUserInfo(String sessionId) throws IOException {
        log.info("sessionId: " + request.getHeader("sessionId"));
        log.info("appPlatform: " + request.getHeader("appPlatform"));
        log.info("appVersion: " + request.getHeader("appVersion"));

        if (Strings.isEmpty(sessionId)) {
            sessionId = request.getHeader("sessionId");
        }

        return sessionService.getSessionInfo(sessionId);
    }

    @StatisticsTime("getUserInfoByOpenId")
    @GetMapping("/getUserInfoByOpenId")
    public BaseRes getUserInfoByOpenId(String openId) {
        return RespUtil.success(userService.getUserInfoByOpenId(openId));
    }

    @StatisticsTime("personCenter")
    @ApiOperation("个人中心")
    @GetMapping("/personCenter")
    public BaseRes personCenter(@OpenId String openId) {
        ApiAssert.checkOpenId(openId);
        StudentDO studentInfo = studentService.getStudentInfo(openId);
        if (studentInfo == null) {
            return RespUtil.error(ResultEnum.JWC_ACCOUNT_NOT_BIND);
        }
        return RespUtil.success(studentInfo);
    }

    @StatisticsTime("modifyStudentInfo")
    @ApiOperation("修改个人信息")
    @GetMapping("/modifyStudentInfo")
    public BaseRes modifyStudentInfo(@OpenId String openId, String jwcAccount, String nickName, String department) {
        ApiAssert.checkOpenId(openId);
        StudentDO studentDO = new StudentDO();
        studentDO.setOpenId(openId);
        UserInfoDO userInfo = userService.getUserInfoByOpenId(openId);
        if (StringUtils.isEmpty(jwcAccount)) {
            jwcAccount = userInfo.getJwcAccount();
        }
        studentDO.setNickName(nickName);
        studentDO.setDepartment(department);
        studentDO.setJwcAccount(jwcAccount);
        userService.updateJwcAccount(openId, jwcAccount);
        studentService.modifyStudentInfo(studentDO);
        return RespUtil.success();
    }

    @StatisticsTime("getAdvisers")
    @ApiOperation("查看所有导师的信息")
    @GetMapping("/getAdvisers")
    public BaseRes getAdvisers(@RequestParam(required = false, defaultValue = "0") int start,
                               @RequestParam(required = false, defaultValue = "100") int offset){
        List<AdviserDO> adviserDOS = adviserService.getAdvisers(start, offset);
        return RespUtil.success(adviserDOS);
    }

    /**
     * 查询导师信息
     *
     * @param id
     * @return
     */
    @StatisticsTime("getAdviser")
    @ApiOperation("根据id获取单个导师信息")
    @GetMapping("/getAdviser")
    public BaseRes getInformationById(@RequestParam int id) {
        return RespUtil.success(adviserService.getAdviser(id));
    }

    /**
     * 增加导师信息
     */
    @StatisticsTime("insertAdviserInfo")
    @ApiOperation("增加导师信息")
    @PostMapping("/insertAdviserInfo")
    public BaseRes insertAdviserInfo(@RequestParam String adviserName,
                                     @RequestParam String adviserTel,
                                     @RequestParam String adviserInfo,
                                     @RequestParam String  department,
                                     @RequestParam String  avatar
    ) {
        if(adviserName==null){
            return RespUtil.error(ResultEnum.ADVISERNAME_IS_NULL);
        }
            AdviserDO adviserDo=new AdviserDO();
            adviserDo.setName(adviserName);
            adviserDo.setTel(adviserTel);
            adviserDo.setInfo(adviserInfo);
            adviserDo.setDepartment(department);
            adviserDo.setAvatar(avatar);
            adviserService.insertAdviserInfo(adviserDo);
            return RespUtil.success();


    }

    /**
     * 修改导师信息
     */
    @StatisticsTime("updateAdviserInfo")
    @ApiOperation("修改导师信息")
    @PostMapping("/updateAdviserInfo")
    public BaseRes updateAdviserInfo(
                                     @RequestParam Integer id,
                                     @RequestParam String adviserName,
                                     @RequestParam String adviserTel,
                                     @RequestParam String adviserInfo,
                                     @RequestParam String  department,
                                     @RequestParam String  avatar){
        AdviserDO adviserDO=adviserService.getAdviser(id);
        adviserDO.setName(adviserName);
        adviserDO.setTel(adviserTel);
        adviserDO.setInfo(adviserInfo);
        adviserDO.setDepartment(department);
        adviserDO.setAvatar(avatar);
        adviserService.updateAdviserInfo(adviserDO);
        return RespUtil.success();
    }


}
