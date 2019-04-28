package com.hang.dao;

import com.hang.CcsuServiceApplicationTests;
import com.hang.pojo.data.CourseDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function:
 */
public class CourseDAOTest extends CcsuServiceApplicationTests {

    @Autowired
    private CourseDAO courseDAO;

    @Test
    public void selectAllCourse() {
        List<CourseDO> courseDOS = courseDAO.selectAllCourseByJwcAccount("B20141004209");
        courseDOS.forEach(System.out::println);
    }

}