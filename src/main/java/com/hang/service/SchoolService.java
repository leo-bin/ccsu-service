package com.hang.service;

import com.google.common.collect.Lists;
import com.hang.dao.CourseDAO;
import com.hang.dao.GradeDAO;
import com.hang.dao.LostPropertyAndRecruitDAO;
import com.hang.dao.StudentDAO;
import com.hang.enums.LostPropertyAndRecruitEnum;
import com.hang.exceptions.ApiException;
import com.hang.pojo.data.CourseDO;
import com.hang.pojo.data.GradeDO;
import com.hang.pojo.data.LostPropertyAndRecruitDO;
import com.hang.pojo.vo.CourseVO;
import com.hang.pojo.vo.LostPropertyAndRecruitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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

    @Autowired
    private LostPropertyAndRecruitDAO lostPropertyAndRecruitDAO;

    @Autowired
    private GradeDAO gradeDAO;

    public List<GradeDO> getGrader(String jwcAccount, String semester) {
        return gradeDAO.selectGradeByJwcAccountAndXnxq(jwcAccount, semester);
    }

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

    public void saveLostPropertyAndRecruit(LostPropertyAndRecruitVO lostPropertyAndRecruitVO, LostPropertyAndRecruitEnum lostPropertyAndRecruitEnum) {
        LostPropertyAndRecruitDO lostPropertyAndRecruitDO = new LostPropertyAndRecruitDO();
        BeanUtils.copyProperties(lostPropertyAndRecruitVO, lostPropertyAndRecruitDO);
        lostPropertyAndRecruitDO.setOccurTime(new Date(lostPropertyAndRecruitVO.getOccurTime()));
        lostPropertyAndRecruitDO.setDatetime(new Date());
        lostPropertyAndRecruitDO.setCategory(lostPropertyAndRecruitEnum.name());
        int i = lostPropertyAndRecruitDAO.insert(lostPropertyAndRecruitDO);
        if (i != 1) {
            throw new ApiException(-1, "存储失败");
        }
    }

    public List<LostPropertyAndRecruitVO> listLostPropertyAndRecruit(LostPropertyAndRecruitEnum lostPropertyAndRecruitEnum, int start, int offset) {
        List<LostPropertyAndRecruitDO> lostPropertyAndRecruitDOS = lostPropertyAndRecruitDAO.listByCategory(lostPropertyAndRecruitEnum.name(), start, offset);
        ArrayList<LostPropertyAndRecruitVO> result = Lists.newArrayList();
        lostPropertyAndRecruitDOS.forEach(e -> {
            LostPropertyAndRecruitVO lostPropertyAndRecruitVO = new LostPropertyAndRecruitVO();
            BeanUtils.copyProperties(e, lostPropertyAndRecruitVO);
            lostPropertyAndRecruitVO.setOccurTime(e.getOccurTime().getTime());
            result.add(lostPropertyAndRecruitVO);
        });
        return result;
    }

    public void removeLostAndRecruit(int id){
        int i=lostPropertyAndRecruitDAO.delete(id);
        if (i != 1) {
            throw new ApiException(-1, "删除失败");
        }
    }

    public Set<String> getFreeClassroom(String semester, String section, String week, String weekDay) {
        Set<String> allClassroom = courseDAO.selectAllClassroom();
        Set<String> classroomNow = courseDAO.selectClassroomNow(semester, section, week, weekDay);
        allClassroom.removeAll(classroomNow);
        return allClassroom;
    }


}
