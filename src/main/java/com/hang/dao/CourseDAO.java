package com.hang.dao;

import com.hang.pojo.data.CourseDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function:
 */
@Repository
public interface CourseDAO {

    /**
     * 查询对应学号的所有课表
     *
     * @param jwcAccount
     * @return
     */
    List<CourseDO> selectAllCourseByJwcAccount(String jwcAccount);

    /**
     * 查询对应学号下对应学期的所有课表
     * @param jwcAccount
     * @param semester
     * @return
     */
    List<CourseDO> selectAllCourseByJwcAccountAndSemester(@Param("jwcAccount") String jwcAccount, @Param("semester") String semester);

}