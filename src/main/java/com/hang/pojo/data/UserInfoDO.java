/*
 * Created by Long Duping
 * Date 2019-02-15 14:42
 */
package com.hang.pojo.data;

import lombok.Data;

import java.util.Date;

/**
 * @author test
 */
@Data
public class UserInfoDO {

    private Long id;

    private String openId;

    private String nickName;

    private String avatarUrl;

    private Integer gender;

    private String city;

    private String province;

    private String country;

    private Integer roleId;

    private String stuNumber;

    private String realName;

    private Date createTime;

    private Date lastLoginTime;

    private String jwcAccount;

}
