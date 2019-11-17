package com.hang.constant;

import lombok.Data;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  LEO-BIN
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

    /**
     * 缓冲页面的url
     */
    public static final String HUANCHON_URL="http://jwcxxcx.ccsu.cn/jwxt/Logon.do?method=logonBySSO";

    /**
     * 内网课表爬虫服务url
     */
    public static final String INSIDE_COURSE_URL="http://free-go.natapp1.cc/school/course";

    /**
     * 内网成绩爬虫服务url
     */
    public static final String INSIDE_GRADE_URL="http://free-go.natapp1.cc/school/grade";


    /**
     * 学号对应的院系
     */
    private  Map<String,String> department;

    /**
     * 初始化
     * key=学院编号
     * value=学院名称
     */
    public SchoolConstant(){
        this.department=new HashMap<>();
        department.put("01", "土木院");
        department.put("02", "机电院");
        department.put("03", "计数院");
        department.put("04", "电信院");
        department.put("05", "生环院");
        department.put("06", "法学院");
        department.put("07", "影传院");
        department.put("08", "外语院");
        department.put("09", "经管院");
        department.put("10", "艺术院");
        department.put("11", "音乐院");
    }


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

    /**
     * 根据学号判断学生属于哪一个院系
     */
    public String getDepartment(String jwcaccount) {
        String a = jwcaccount.substring(5, 7);
        for (Map.Entry<String, String> Ids : department.entrySet()) {
            if (Ids.getKey().equals(a)) {
                return Ids.getValue();
            }
        }
        return null;
    }
}
