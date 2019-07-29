package com.hang.pojo.data;

import lombok.Data;

/**
 * @author hangs.zhang
 * @date 2019/1/26
 * *****************
 * function: 团队实体类
 */
@Data
public class TeamDO {

    private Integer id;

    private String name;

    private String honor;

    private String log;

    private String members;

    private String avatar;

    private String advisor;

    /**
     * 团队状态：1-已节课，2-未结课
     */
    private Integer state;

}
