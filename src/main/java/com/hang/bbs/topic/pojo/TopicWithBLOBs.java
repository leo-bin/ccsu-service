package com.hang.bbs.topic.pojo;

import lombok.Data;

@Data
public class TopicWithBLOBs extends Topic {

    private String content;

    private String downIds;

    private String upIds;

}