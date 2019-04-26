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
public class SystemInfoDTO implements Serializable {

    /**
     * 手机品牌
     */
    private String brand;

    /**
     * 手机型号
     */
    private String model;

    /**
     * 设备像素比
     */
    private int pixelRatio;

    /**
     * 屏幕宽度
     */
    private int screenWidth;

    /**
     * 屏幕高度
     */
    private int screenHeight;

    /**
     * 可使用窗口宽度
     */
    private int windowWidth;

    /**
     * 可使用窗口高度
     */
    private int windowHeight;

    /**
     * 状态栏的高度
     */
    private int statusBarHeight;

    /**
     * 微信设置的语言
     */
    private String language;

    /**
     * 微信版本号
     */
    private String wxVersion;

    /**
     * 操作系统版本
     */
    private String systemVersion;

    /**
     * 客户端平台
     */
    private String platform;

    /**
     * "用户字体大小设置。以“我-设置-通用-字体大小”中的设置为准，单位 px。
     */
    private int fontSizeSetting;

    /**
     * 客户端基础库版本
     */
    private String SDKVersion;

    /**
     * 性能等级，-2 或 0：该设备无法运行小游戏，-1：性能未知，>=1 设备性能值，该值越高，设备性能越好 (目前设备最高不到50)
     */
    private int benchmarkLevel;

}
