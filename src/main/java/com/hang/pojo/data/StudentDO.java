package com.hang.pojo.data;

import lombok.Data;

/**
 * @author zhanghang
 */
@Data
public class StudentDO {

    private Integer id;

    private String openId;

    private String nickName;

    private String jwcAccount;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 学生头衔，具体班级或者是其他的称呼
     */
    private String title;

    /**
     * 微信头像url
     */
    private String avatar;

    /**
     * 学生年级
     */
    private String grade;

    /**
     * 院系
     */
    private String department;

    /**
     * 密码
     */
    private String code;

}
