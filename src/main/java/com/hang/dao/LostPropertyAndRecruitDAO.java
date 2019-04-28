package com.hang.dao;

import com.hang.pojo.data.LostPropertyAndRecruitDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function:
 */
public interface LostPropertyAndRecruitDAO {

    /**
     * @param lostPropertyAndRecruitDO
     * @return
     */
    int insert(LostPropertyAndRecruitDO lostPropertyAndRecruitDO);

    List<LostPropertyAndRecruitDO> listByCategory(@Param("category") String category);

    List<LostPropertyAndRecruitDO> listAll();

}
