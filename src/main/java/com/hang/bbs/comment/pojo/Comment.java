package com.hang.bbs.comment.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author test
 */
@Data
public class Comment {

  private Integer id;

  private Integer commentId;

  private Integer down;

  private Date inTime;

  private Integer topicId;

  private Integer up;

  private String openId;

}