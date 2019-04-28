package com.hang.service;

import com.hang.CcsuServiceApplicationTests;
import com.hang.pojo.data.UserInfoDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function:
 */
public class UserServiceTest extends CcsuServiceApplicationTests {

    @Autowired
    private UserService userService;

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
}