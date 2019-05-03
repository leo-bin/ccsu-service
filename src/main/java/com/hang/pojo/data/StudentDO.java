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

    private Double qualityFraction;

    private Double comprehensiveFraction;

    private String grade;

    /**
     * 院系
     */
    private String department;

}
