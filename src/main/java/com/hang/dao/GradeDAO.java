package com.hang.dao;

import com.hang.pojo.data.GradeDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhanghang
 */
@Repository
public interface GradeDAO {

    /**
     * 根据学号和学期查询成绩
     */
    List<GradeDO> selectGradeByJwcAccountAndXnxq(@Param("jwcAccount") String jwcAccount, @Param("xnxq") String xnxq);


    /**
     * 用户第一次查询成绩，做模拟登陆爬取成绩
     */
    int addGrade(GradeDO gradeDO);

}
