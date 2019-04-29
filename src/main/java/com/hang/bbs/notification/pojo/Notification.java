package com.hang.bbs.notification.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author test
 */
@Data
public class Notification {

    private Integer id;

    private String action;

    private Date inTime;

    private Boolean isRead;

    private String targetOpenId;

    private Integer topicId;

    private String openId;

    private String content;

}