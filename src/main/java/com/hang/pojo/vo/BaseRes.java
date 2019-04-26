package com.hang.pojo.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.hang.pojo.data.InformationDO;
import lombok.Data;

/**
 * @author hangs.zhang
 * @date 2019/1/25
 * *****************
 * function:
 */
@Data
@JsonView(InformationDO.SimpleInformation.class)
public class BaseRes<T> {

    /**
     * 错误代码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    private T data;

}
