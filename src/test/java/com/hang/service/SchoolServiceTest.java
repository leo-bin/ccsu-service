package com.hang.service;

import com.hang.CcsuServiceApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function:
 */
public class SchoolServiceTest extends CcsuServiceApplicationTests {

    @Autowired
    private SchoolService schoolService;

    /**
     *

    @Test
    public void getFreeClassroom() {
        Set<String> freeClassroom = schoolService.getFreeClassroom("2018-2019-2", "1-2", "5", "2");
        System.out.println("freeClassroom : " + freeClassroom.size());
    }

    */


}