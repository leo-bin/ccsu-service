package com.hang.manage;

import com.hang.pojo.data.UserInfoDO;
import com.hang.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hang.constant.CacheConstant.USER_OPEN_ID_PREFIX;

/**
 * @author zhanghang
 */
@Service
public class UserCache {

    @Autowired
    private RedisUtil redisUtil;

    public void saveUserInfo(String openId, UserInfoDO userInfoDO) {
        redisUtil.set(USER_OPEN_ID_PREFIX + openId, userInfoDO);
    }

    public UserInfoDO getUserInfo(String openId) {
        return (UserInfoDO) redisUtil.get(USER_OPEN_ID_PREFIX + openId);
    }

}
