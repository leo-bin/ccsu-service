package com.hang.service;

import com.hang.CcsuServiceApplicationTests;
import com.hang.dao.UserInfoDAO;
import com.hang.pojo.data.TeacherDO;
import com.hang.pojo.data.UserInfoDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;


public class TeacherServiceTest extends CcsuServiceApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoDAO userInfoDAO;

    @Autowired
    private TeacherService teacherService;

    @Test
    public void bindForTeacher() {
        String openId="dujhdsbshghsdghsdhds1564s";
        String code="Z20060751";
        String account="潘怡";
        String name="潘怡";
        UserInfoDO userInfoDO=new UserInfoDO();
        userInfoDO.setCountry("中国");
        userInfoDO.setGender(2);
        userInfoDO.setNickName(name);
        userInfoDO.setOpenId(openId);
        userInfoDO.setJwcAccount(code);
        userInfoDAO.insert(userInfoDO);
        userService.updateJwcAccount(openId,code);
        UserInfoDO userInfo=userInfoDAO.selectByOpenId(openId);
        TeacherDO teacherDO=new TeacherDO();
        teacherDO.setStaffNum(userInfo.getJwcAccount());
        teacherDO.setCode(code);
        teacherDO.setNickName(name);
        teacherDO.setOpenId(userInfo.getOpenId());
        teacherDO.setName(name);
        TeacherDO teacherInfo=teacherService.getTeacherInfo(openId);
        if (Objects.isNull(teacherInfo)){
            teacherService.saveTeacherInfo(teacherDO);
            userService.updateUserRole(openId,1);
        }
        else{
            teacherService.modifyTeacherInfo(teacherDO);
        }
    }
}