package com.hang.dao;

import com.hang.CcsuServiceApplicationTests;
import com.hang.pojo.data.CourseDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

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

    @Test
    public void selectAllClassroom() {
        Set<String> allClassroom = courseDAO.selectAllClassroom();
        allClassroom.forEach(System.out::println);
        System.out.println("********");
        System.out.println(allClassroom.size());
    }


    @Test
    public void selectClassroomNow() {
        Set<String> classroomNow = courseDAO.selectClassroomNow("2018-2019-2", "1-2", "5", "2");
        classroomNow.forEach(System.out::println);
        System.out.println("********");
        System.out.println(classroomNow.size());
    }

}