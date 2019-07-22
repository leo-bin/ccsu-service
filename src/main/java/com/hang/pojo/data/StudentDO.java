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
     * 素拓
     */
    private Double qualityFraction;

    /**
     * 综测
     */
    private Double comprehensiveFraction;

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
