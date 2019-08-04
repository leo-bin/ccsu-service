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
import com.hang.manage.UserCache;
import com.hang.pojo.data.StudentDO;
import com.hang.pojo.data.TeacherDO;
import com.hang.pojo.data.UserInfoDO;
import com.hang.pojo.vo.BaseRes;
import com.hang.service.*;
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
    private TeacherService teacherService;

    @Autowired
    private UserCache userCache;


    /**
     * 绑定账户信息
     * @param openId
     * @param account
     * @return
     */
    @StatisticsTime("bind")
    @ApiOperation("绑定账户信息，openId参数不用传")
    @PostMapping("/bind")
    public BaseRes bind(@OpenId String openId, @RequestParam String account,@RequestParam String code) {
        log.info("account:{}, openId:{}", account, openId);
        if ("B".equals(account.substring(0,1))||"b".equals(account.substring(0,1))){
            studentService.bindForStudent(openId, account,code);
            return RespUtil.success();
        }
        else if ("Z".equals(code.substring(0,1))&&code.length()==9){
            teacherService.bindForTeacher(openId, account,code);
            return RespUtil.success();
        }
        else{
            return RespUtil.error(ResultEnum.STAFF_NUMBER_ERROR);
        }
    }

    /**
     * 用户登陆
     * @param code
     * @param rawData
     * @return
     */
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
    @ApiOperation("根据sessionId获取用户信息")
    @GetMapping("/getUserInfo")
    public BaseRes getUserInfo(String sessionId) throws IOException {
        log.info("sessionId: " + request.getHeader("sessionId"));
        log.info("appPlatform: " + request.getHeader("appPlatform"));
        if (Strings.isEmpty(sessionId)) {
            sessionId = request.getHeader("sessionId");
        }
        return sessionService.getSessionInfo(sessionId);
    }


    @StatisticsTime("getUserInfoByOpenId")
    @ApiOperation("根据openId获取用户信息")
    @GetMapping("/getUserInfoByOpenId")
    public BaseRes getUserInfoByOpenId(@OpenId String openId) {
        return RespUtil.success(userService.getUserInfoByOpenId(openId));
    }


    /**
     * 用户个人中心
     * 根据角色Id判断返回对象
     * @param openId
     * @return
     */
    @StatisticsTime("personCenter")
    @ApiOperation("个人中心")
    @GetMapping("/personCenter")
    public BaseRes personCenter(@OpenId String openId) {
        ApiAssert.checkOpenId(openId);
        StudentDO studentInfo=null;
        TeacherDO teacherDO=null;
        Integer roleId=userService.getUserInfoByOpenId(openId).getRoleId();
        if (roleId.equals(0)||roleId.equals(2)){
           studentInfo = studentService.getStudentInfoByOpenId(openId);
            if (studentInfo == null) {
                return RespUtil.error(ResultEnum.ACCOUNT_NOT_BIND);
            }
            return RespUtil.success(studentInfo);
        }
        else{
            teacherDO=teacherService.getTeacherInfo(openId);
            if (teacherDO==null){
                return RespUtil.error(ResultEnum.ACCOUNT_NOT_BIND);
            }
        }
        return RespUtil.success(teacherDO);
    }


    /**
     * 教师给学生授权
     */
    @StatisticsTime("authorizeToStudent")
    @ApiOperation("教师给学生授权")
    @RequestMapping("/authorizeToStudent")
    public BaseRes authorizeToStudent(@OpenId String openId,@RequestParam String jwcAccount){
        ApiAssert.checkOpenId(openId);
        UserInfoDO teacherInfo = userService.getUserInfoByOpenId(openId);
        UserInfoDO studentInfo=userService.getUserInfoByJwcAccount(jwcAccount);
        Integer roleId=teacherInfo.getRoleId();
        if(roleId.equals(1)&&studentInfo!=null){
            teacherService.authorizeToStudent(jwcAccount);
            //更新缓存
            UserInfoDO userInfoDO=userService.getUserInfoByJwcAccount(jwcAccount);
            userCache.updateUserInfo(studentInfo.getOpenId(),userInfoDO);
            return RespUtil.success();
        }
        else if (StringUtils.isEmpty(studentInfo.getOpenId())){
            return RespUtil.success(ResultEnum.CAN_NOT_GET_USER_INFO);
        }
        else{
            return RespUtil.success(ResultEnum.AUTHORIZE_ERROR);
        }
    }
}
