package com.hang.pojo.data;

import lombok.Data;

/**
 * 导师实体类
 *
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
     * 导师信息
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
}
