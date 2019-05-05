/*
 * Created by Long Duping
 * Date 2019-02-15 14:32
 */
package com.hang.dao;

import com.hang.pojo.data.UserInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author test
 */
@Repository
public interface UserInfoDAO {

    UserInfoDO selectByOpenId(@Param("openId") String openId);

    int updateJwcAccount(@Param("openId") String openId, @Param("jwcAccount") String jwcAccount);

    int insert(UserInfoDO userInfo);

    boolean isExist(@Param("openId") String openId);

    boolean updateLastLoginTime(String openId);
}
