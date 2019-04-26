/*
 * Created by Long Duping
 * Date 2018/12/6 14:18
 */
package com.hang.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 业务统一请求头
 * @author test
 */
@Data
public class RequestHeaderDTO implements Serializable {

    /**
     * 请求客户端平台名称
     */
    private String appPlatform;

    /**
     * 应用版本号
     */
    private String appVersion;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 位置信息
     */
    private PositionInfoDTO positionInfo;

    /**
     * 系统信息
     */
    private SystemInfoDTO systemInfo;

}
