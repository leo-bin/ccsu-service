package com.hang.pojo.data;

import lombok.Data;

import java.util.Date;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function: 失物 招领 Recruit
 */
@Data
public class LostPropertyAndRecruitDO {

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
     * 具体信息
     */
    private String initiatorMessage;

    /**
     * 发生地点
     */
    private String initiatorLocation;

    /**
     * 发生时间
     */
    private Date occurTime;

    /**
     * 联系方式
     */
    private String contactInformation;

    /**
     * LostProperty 失物
     * Recruit 招领
     */
    private String category;

    private Date datetime;

}
