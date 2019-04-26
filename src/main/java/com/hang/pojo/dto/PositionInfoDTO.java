/*
 * Created by Long Duping
 * Date 2018/12/6 14:21
 */
package com.hang.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author test
 */
@Data
public class PositionInfoDTO implements Serializable {

    /**
     * 纬度，范围为 -90~90，负数表示南纬
     */
    private long latitude;

    /**
     * 经度，范围为 -180~180，负数表示西经
     */
    private long longitude;

    /**
     * 速度，单位 m/s
     */
    private int speed;

    /**
     * 位置的精确度
     */
    private int accuracy;

    /**
     * 高度，单位 m
     */
    private int altitude;

    /**
     * 垂直精度，单位 m（Android 无法获取，返回 0）
     */
    private int verticalAccuracy;

    /**
     * 水平精度，单位 m
     */
    private int horizontalAccuracy;

}
