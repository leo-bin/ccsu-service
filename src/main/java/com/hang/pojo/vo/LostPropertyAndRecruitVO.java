package com.hang.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function: 失物 招领 Recruit
 */
@Data
public class LostPropertyAndRecruitVO {

    /**
     * 编号
     */
    private Integer id;
    /**
     * 发起者
     */
    private String initiatorName;

    /**
     * 学号
     */
    private String initiatorJwcAccount;

    /**
     * 信息
     */
    private String initiatorMessage;

    /**
     * 地点
     */
    private String initiatorLocation;

    /**
     * 发生时间
     */
    private Long occurTime;

    /**
     * 联系方式
     */
    private String contactInformation;

}
