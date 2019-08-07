package com.hang.pojo.data;

import lombok.Data;

/**
 * @author leo-bin
 * @date 2019/6/19
 * @apiNote 导师实体类
 */
@Data
public class AdviserDO {

    /**
     * 导师id
     */
    private Integer id;

    /**
     * 导师姓名
     */
    private String name;

    /**
     * 导师联系方式
     */
    private String tel;

    /**
     * 导师个人信息简介
     */
    private String info;

    /**
     * 院系
     */
    private String department;

    /**
     * 导师头像
     */
    private String avatar;

    /**
     * 个人邮箱
     */
    private String email;

    /**
     * 办公地点
     */
    private String office;

    /**
     * 学历
     */
    private String education;

    /**
     * 现任职位
     */
    private String position;

    /**
     * 主讲课程
     */
    private String teachingCourse;

    /**
     * 研究方向
     */
    private String researchDirection;

}
