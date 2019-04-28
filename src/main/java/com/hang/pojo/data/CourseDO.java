package com.hang.pojo.data;

import lombok.Data;

import java.util.Date;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function:
 */
@Data
public class CourseDO {

    private Integer id;

    private String jwcAccount;

    private String weekday;

    private String classTime;

    private String section;

    private String subjectName;

    private String className;

    private String teacher;

    private String weekSeq;

    private String weekStr;

    private String location;

    private String xnxqh;

    private Date datetime;

}
