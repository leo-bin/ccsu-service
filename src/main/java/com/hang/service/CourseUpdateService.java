package com.hang.service;


import com.hang.dao.CourseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author free-go
 * @Date Created in 18:09 2019/8/29
 **/

@Service
public class CourseUpdateService {

    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private CourseCrawlerService courseCrawlerService;

    public void turnToCourse(String jwcAccount, String semester, String password) {
        courseDAO.deleteCourse(jwcAccount,semester);
        courseCrawlerService.turnToCourse(jwcAccount,password);
    }
}
