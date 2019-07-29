package com.hang.constant;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class SchoolConstantTest {

    /**
     * 获根据当前时间自动取学期
     */
    @Test
    public void getTerm() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int xueqi = 1;
        String year0 = String.valueOf(y - 1);
        String year1 = String.valueOf(y);
        String year2 = String.valueOf(y + 1);
        String xnxq="";
        if (month >= 1 && month <= 2) {
            xnxq= (year0 + "-" + year1 + "-" + xueqi);
        }
        if (month >= 3 && month <= 8) {
            xueqi = 2;
            xnxq=  (year0 + "-" + year1 + "-" + xueqi);
        }
        if (month >= 9 && month <= 12) {
            xnxq=  (year1 + "-" + year2 + "-" + xueqi);
        }
        System.out.println(xnxq);
    }

    @Test
    public void getDepartment(){
        Map<String,String> department=new HashMap<>();
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
        String jwcaccount="B20170302310";
        String a = jwcaccount.substring(5, 7);
        for (Map.Entry<String, String> Ids : department.entrySet()) {
            if (Ids.getKey().equals(a)) {
                System.out.println("你的院系为："+Ids.getValue());
                break;
            }
        }
    }

}