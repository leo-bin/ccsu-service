package com.hang.constant;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;


public class SchoolConstantTest {

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


}