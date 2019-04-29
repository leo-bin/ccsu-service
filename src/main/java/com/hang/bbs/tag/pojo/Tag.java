package com.hang.bbs.tag.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Tag {

  private Integer id;

  private Date inTime;

  private String intro;

  private String logo;

  private String name;

  private Integer topicCount;

}