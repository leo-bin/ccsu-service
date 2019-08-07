package com.hang.pojo.data;

import lombok.Data;

import java.util.Date;

/**
 * @author test
 */
@Data
public class NotificationDO {

    private Integer id;

    /**
     * 通知所属行为
     */
    private String action;

    private Date inTime;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 消息目标openId
     */
    private String targetOpenId;

    /**
     * 通知Id，包括系统通知和评论消息的通知
     */
    private Integer notificationId;

    private String openId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 额外注释
     */
    private String notes;
}