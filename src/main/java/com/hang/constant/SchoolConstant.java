package com.hang.constant;

import lombok.Data;

import java.util.Calendar;

/**
 * @author  BIN
 * @date 2019/7/18
 * function:
 * 学校服务常量
 */
@Data
public class SchoolConstant {

    /**
     * 教务处网址
     */
    public static final String LOGIN_URL = "http://jwcxxcx.ccsu.cn/jwxt/Logon.do?method=logon";

    private String xueqi;

    /**
     * 根据当前时间计算出现在处于学校的哪一学期
     * @return
     */
    public String getTerm() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int xueqi = 1;
        String year0 = String.valueOf(y - 1);
        String year1 = String.valueOf(y);
        String year2 = String.valueOf(y + 1);
        if (month >= 1 && month <= 2) {
            return (year0 + "-" + year1 + "-" + xueqi);
        }
        if (month >= 3 && month <= 8) {
            xueqi = 2;
            return (year0 + "-" + year1 + "-" + xueqi);
        }
        if (month >= 9 && month <= 12) {
            return (year1 + "-" + year2 + "-" + xueqi);
        }
        return null;
    }
}
