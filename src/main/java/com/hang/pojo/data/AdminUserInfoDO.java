package com.hang.pojo.data;

import lombok.Data;

/**
 * @author LEO-BIN
 * @date 2019/6/20
 * 管理员信息实体类
 */
@Data
public class AdminUserInfoDO {

    private long id;

    /**
     * 管理员账号
     */
    private String userName;

    /**
     * 账号密码
     */
    private String userPwd;

}
