package com.hang.service;


import com.hang.dao.CourseDAO;
import com.hang.exceptions.ApiException;
import com.hang.pojo.data.CourseDO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 更新课表
     */
    @Transactional(rollbackFor = ApiException.class)
    public Integer turnToCourse(String jwcAccount, String semester, String password) {
        List<CourseDO> courseDOS = courseDAO.selectAllCourseByJwcAccountAndSemester(jwcAccount, semester);
        if (courseDOS.size() > 0) {
            Integer flag=courseDAO.deleteCourse(jwcAccount, semester);
        }
        return courseCrawlerService.turnToCourseSpider(jwcAccount, password);
    }
}
