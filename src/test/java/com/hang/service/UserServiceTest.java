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

}