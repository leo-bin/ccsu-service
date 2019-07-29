package com.hang.pojo.vo;

import lombok.Data;

/**
 * @author hangs.zhang
 * @date 2019/1/25
 * *****************
 * function:
 */
@Data
public class GroupMemberVO {

    private String avatar;

    private String name;

    private String title;

    /**
     * 角色：组长，组员
     */
    private String role;

}
