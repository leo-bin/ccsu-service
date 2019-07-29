package com.hang.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author hangs.zhang
 * @date 2019/1/25
 * *****************
 * function:
 */
@Data
public class TeamVO {

    private Integer id;

    private String avatar;

    private String name;

    /**
     * 组员
     */
    private List<GroupMemberVO> groupMemberVOS;

    /**
     * 导师
     */
    private String advisor;

    /**
     * 团队状态：1-已节课，2-未结课
     */
    private Integer state;

    /**
     * 团队项目
     */
    private List<ProjectVO> projects;

    /**
     * 团队荣誉
     */
    private List<String> honor;

    /**
     * 团队日志
     */
    private List<TeamLogVO> teamLog;



}
