package com.hang.utils;


import com.alibaba.fastjson.JSON;
import com.hang.enums.ResultEnum;
import com.hang.pojo.vo.BaseRes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author hangs.zhang
 * @date 2018/12/10
 * *****************
 * function:
 */
public class RespUtil {

    public static BaseRes success(Object obj) {
        BaseRes baseRes = new BaseRes();
        baseRes.setErrcode(ResultEnum.SUCCESS.getCode());
        baseRes.setErrmsg(ResultEnum.SUCCESS.getMsg());
        baseRes.setData(obj);
        return baseRes;
    }

    public static BaseRes success() {
        return success(null);
    }

    public static BaseRes error(Integer code, String msg) {
        BaseRes baseRes = new BaseRes();
        baseRes.setErrcode(code);
        baseRes.setErrmsg(msg);
        baseRes.setData(null);
        return baseRes;
    }

    public static BaseRes error(ResultEnum resultEnum) {
        BaseRes baseRes = new BaseRes();
        baseRes.setErrcode(resultEnum.getCode());
        baseRes.setErrmsg(resultEnum.getMsg());
        baseRes.setData(null);
        return baseRes;
    }

    public static void render(HttpServletResponse response, ResultEnum resultEnum) throws IOException {
        PrintWriter out = response.getWriter();
        String result = JSON.toJSONString(RespUtil.error(resultEnum));
        out.println(result);
        out.flush();
        out.close();
    }

}
