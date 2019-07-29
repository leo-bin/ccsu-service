package com.hang.pojo.data;

import lombok.Data;

import java.util.Date;

/**
 * @author leo-bin
 * @date 2019/7/27 21:23
 * @function 系统通知实体类
 */
@Data
public class SystemNotificationDO {

    private Integer id;

    /**
     * 通知类别
     */
    private String noteType;

    /**
     * 通知内容
     */
    private String message;

    /**
     * 通知发送时间
     */
    private Date sendTime;
}
