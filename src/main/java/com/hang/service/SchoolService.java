package com.hang.service;

import com.google.common.collect.Lists;
import com.hang.dao.CourseDAO;
import com.hang.pojo.data.CourseDO;
import com.hang.pojo.vo.CourseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function: 学期
 */
@Slf4j
@Service
public class SchoolService {

    @Autowired
    private CourseDAO courseDAO;

    public List<CourseVO> getCourseByWeek(String jwcAccount, Integer week, String semester) {
        List<CourseVO> result = Lists.newArrayList();
        List<CourseDO> courseDOS = courseDAO.selectAllCourseByJwcAccount(jwcAccount);
        List<CourseDO> collect = courseDOS.stream()
                .filter(e -> Lists.newArrayList(e.getWeekSeq().split("-")).contains(String.valueOf(week)))
                .collect(Collectors.toList());

        collect.forEach(e -> {
            String[] split = e.getWeekSeq().split("-");
            for (String weekStr : split) {
                if (String.valueOf(week).equals(weekStr.trim())) {
                    CourseVO courseVO = new CourseVO();
                    courseVO.setWeek(weekStr);
                    BeanUtils.copyProperties(e, courseVO);
                    result.add(courseVO);
                }
            }
        });
        return result;
    }

    public List<CourseDO> getAllCourse(String jwcAccount, String semester) {
        return courseDAO.selectAllCourseByJwcAccountAndSemester(jwcAccount, semester);
    }

}
