package com.hang.manage;

import com.hang.pojo.data.UserInfoDO;
import com.hang.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hang.constant.CacheConstant.USER_OPEN_ID_PREFIX;

/**
 * @author zhanghang
 * @apiNote  用户redis缓存接口
 */
@Service
public class UserCache {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 保存用户信息
     */
    public void saveUserInfo(String openId, UserInfoDO userInfoDO) {
        redisUtil.set(USER_OPEN_ID_PREFIX + openId, userInfoDO);
    }

    /**
     * 查询用户信息
     */
    public UserInfoDO getUserInfo(String openId) {
        return (UserInfoDO) redisUtil.get(USER_OPEN_ID_PREFIX + openId);
    }

    /**
     * 更新用户信息
     *
     * @apiNote 由于redis中没有直接的更新的接口，这里采用先找到，在删除，然后在重新写入的方法
     */
    public void updateUserInfo(String openId, UserInfoDO userInfo) {
        UserInfoDO userInfoDO = (UserInfoDO) redisUtil.get(USER_OPEN_ID_PREFIX + openId);
        if (userInfoDO != null) {
            redisUtil.del(USER_OPEN_ID_PREFIX + openId);
            redisUtil.set(USER_OPEN_ID_PREFIX + openId, userInfo);
        }
    }
}
