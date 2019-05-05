package com.hang.dao;

import com.hang.pojo.data.StudentDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanghang
 */
@Repository
public interface StudentDAO {

    /**
     * 根据openId查询信息
     *
     * @param openId
     * @return
     */
    StudentDO selectStudentDOByOpenId(@Param("openId") String openId);

    /**
     * 插入
     *
     * @param studentDO
     * @return
     */
    int insert(StudentDO studentDO);

    /**
     * 更新数据
     *
     * @param studentDO
     * @return
     */
    int update(StudentDO studentDO);

    int updateComprehensiveFraction(@Param("comprehensiveFraction") Double comprehensiveFraction, @Param("checkOpenId") String openId);

    List<StudentDO> list(@Param("start") int start, @Param("offset") int offset);

}
