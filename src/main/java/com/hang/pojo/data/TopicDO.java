package com.hang.pojo.data;

import lombok.Data;

import java.util.Date;

/**
 * @author zhanghang
 */
@Data
public class TopicDO {

    private Integer id;

    private Integer commentCount;

    private Integer down;

    private Boolean good;

    private Date lastCommentTime;

    private Date modifyTime;

    /**
     * 标签
     */
    private String tag;

    private String title;

    /**
     * 是否置顶
     */
    private Boolean top;

    private Integer up;

    private Integer openId;

    /**
     * 展示数
     */
    private Integer view;

}
