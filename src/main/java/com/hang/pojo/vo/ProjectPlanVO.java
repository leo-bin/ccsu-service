package com.hang.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author leo-bin
 * @date 2019/8/19 16:58
 * @apiNote
 */
@Data
@AllArgsConstructor
public class ProjectPlanVO {

    /**
     * planId
     */
    private Integer id;

    /**
     * 计划开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 具体计划描述
     */
    private String description;
}
