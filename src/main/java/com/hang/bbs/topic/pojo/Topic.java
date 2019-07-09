package com.hang.bbs.topic.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author test
 */
@Data
public class Topic {

  private Integer id;

  private Integer commentCount;

  private Integer down;

  private Boolean good;

  /**
   * 这里的时间类型设置成String
   */
  private String inTime;

  private Date lastCommentTime;

  private Date modifyTime;

  private String tag;

  private String title;

  private Boolean top;

  private Integer up;

  private String openId;

  private Integer view;

  private Double weight;

}