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

    UserInfoDO selectByOpenId(@Param("checkOpenId") String openId);

    int updateJwcAccount(@Param("checkOpenId") String openId, @Param("jwcAccount") String jwcAccount);

    int insert(UserInfoDO userInfo);

    boolean isExist(@Param("checkOpenId") String openId);

    boolean updateLastLoginTime(String openId);
}
