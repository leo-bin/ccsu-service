package com.hang.pojo.vo;

import lombok.Data;

/**
 * @author hangs.zhang
 * @date 19-5-11
 * *****************
 * function:
 */
@Data
public class ApplyMessageVO {

    private String openId;

    private Integer applyId;

    private String jwcAccount;

    private String nickName;

    private String activityName;

    private String status;

    private String statusMessage;

}
