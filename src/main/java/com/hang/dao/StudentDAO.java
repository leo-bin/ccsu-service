package com.hang.dao;

import com.hang.pojo.data.StudentDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

}
