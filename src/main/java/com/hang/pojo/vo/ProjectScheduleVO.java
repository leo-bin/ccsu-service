package com.hang.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author hangs.zhang
 * @date 19-5-7
 * *****************
 * function:
 */
@Data
@AllArgsConstructor
public class ProjectScheduleVO {
    private Integer id;

    private Date time;

    private String content;
}
