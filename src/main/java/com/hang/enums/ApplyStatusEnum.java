package com.hang.enums;

import lombok.Getter;

/**
 * @author test
 * @date 19-4-26
 * *****************
 * function:
 */
@Getter
public enum ApplyStatusEnum {
    /**
     * 申请中
     */
    CURRENT_APPLY("申请中"),
    /**
     * 成功
     */
    SUCCESS("申请成功"),
    /**
     * 失败
     */
    FAILURE("申请失败");

    private String messgae;

    ApplyStatusEnum(String messgae) {
        this.messgae = messgae;
    }

    public static ApplyStatusEnum getByName(String name) {
        ApplyStatusEnum[] values = ApplyStatusEnum.values();
        for (ApplyStatusEnum applyStatusEnum : values) {
            if (applyStatusEnum.name().equals(name)) {
                return applyStatusEnum;
            }
        }
        return null;
    }

}
