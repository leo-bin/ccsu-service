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
     * 根据学号的学期查询成绩
     *
     * @param jwcAccount
     * @param xnxq
     * @return
     */
    List<GradeDO> selectGradeByJwcAccountAndXnxq(@Param("jwcAccount") String jwcAccount, @Param("xnxq") String xnxq);

}
