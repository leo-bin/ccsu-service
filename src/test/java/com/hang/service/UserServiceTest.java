package com.hang.service;

import com.hang.CcsuServiceApplicationTests;
import com.hang.pojo.data.AdviserDO;
import com.hang.pojo.data.UserInfoDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function:
 */
public class UserServiceTest extends CcsuServiceApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private AdviserService adviserService;

    private static final String openId = "o8Tn70BKPhACUHANmCmh0S9amdkM";


    @Test
    public void selectUserInfoByOpenId() {
        UserInfoDO userInfoDO = userService.getUserInfoByOpenId(openId);
        System.out.println(userInfoDO);
    }

    @Test
    public void updateJwcAccount() {
        userService.updateJwcAccount(openId, "b20160304410");
    }


    @Test
    public void getAdvisers() {
        List<AdviserDO> adviserDOS =adviserService.getAdvisers(0,100);
        if(adviserDOS ==null){
            System.out.println("查询失败！！");
        }
        else
        System.out.println(adviserDOS);
    }

    @Test
    public void getAdviser() {
        AdviserDO adviserDo=adviserService.getAdviser(1);
        if(adviserDo==null){
            System.out.println("查询失败！！");
        }
        else
            System.out.println(adviserDo);
    }

    @Test
    public void insertAdviser(){
        AdviserDO adviserDO=new AdviserDO();
        adviserDO.setId(14);
        adviserDO.setName("李四");
        adviserDO.setTel("151651");
        adviserDO.setInfo("hhd");
        adviserDO.setDepartment("shh");
        adviserDO.setAvatar("dysgdj");
        adviserDO.setEmail("465464@qq.com");
        adviserDO.setOffice("djuhd");
        adviserDO.setEducation("hduhs");
        adviserDO.setPosition("dhdh");
        adviserDO.setTeachingCourse("dhuhd");
        adviserDO.setResearchDirection("dlhdkudhk");
        AdviserDO adviserDO2=adviserService.getAdviser(14);
        if(adviserDO2!=null){
            System.out.println("导师已经存在");
        }
        else{
            adviserService.insertAdviserInfo(adviserDO);
        }

    }

    @Test
    public void updateAdviserInfo(){
        AdviserDO adviserDO=adviserService.getAdviser(1);
        adviserDO.setName("潘怡");
        adviserDO.setTel("18230694688");
        adviserDO.setInfo("sjhduggdjs");
        adviserDO.setDepartment("计数学院");
        adviserDO.setAvatar("www.deideidei.top");
        adviserDO.setEmail("151465@qq.com");
        adviserDO.setOffice("致远楼1609");
        adviserDO.setEducation("研究生");
        adviserDO.setPosition("教授");
        adviserDO.setTeachingCourse("数据库");
        adviserDO.setResearchDirection("大数据挖掘");
        adviserService.updateAdviserInfo(adviserDO);
    }

}