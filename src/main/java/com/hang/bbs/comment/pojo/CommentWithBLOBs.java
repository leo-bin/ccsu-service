package com.hang.bbs.comment.pojo;

import lombok.Data;

/**
 * @author test
 */
@Data
public class CommentWithBLOBs extends Comment {

    private String content;

    private String downIds;

    private String upIds;

    private String openId;

}