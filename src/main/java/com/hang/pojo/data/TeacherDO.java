package com.hang.pojo.data;

import lombok.Data;

/**
 * @author BIN
 * @date 2019/7/15
 */
@Data
public class TeacherDO {

    private Integer id;

    /**
     * 教工号
     */
    private String  staffNum;

    /**
     * 教师姓名
     */
    private String name;

    /**
     * 密码
     */
    private String code;


    private String openId;

    /**
     * 微信昵称
     */
    private String nickName;

    /**
     * 所属院系
     */
    private String department;

}
