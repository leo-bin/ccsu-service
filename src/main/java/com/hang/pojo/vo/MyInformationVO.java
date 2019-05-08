package com.hang.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author hangs.zhang
 * @date 19-5-8
 * *****************
 * function:
 */
@Data
public class MyInformationVO {

    private String title;

    private Date releaseTime;

    private String authors;

    private String status;

    private String statusMessage;

}
