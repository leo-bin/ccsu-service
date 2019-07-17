package com.hang.dao;

import com.hang.pojo.data.LostPropertyAndRecruitDO;
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
public interface LostPropertyAndRecruitDAO {

    /**
     * 查询失物招领信息
     */
    LostPropertyAndRecruitDO selectLostPropertyAndRecruit(int id);

    int insert(LostPropertyAndRecruitDO lostPropertyAndRecruitDO);

    List<LostPropertyAndRecruitDO> listByCategory(@Param("category") String category, @Param("start") int start,
                                                  @Param("offset") int offset);

    List<LostPropertyAndRecruitDO> listAll(@Param("start") int start, @Param("offset") int offset);

    int delete(int id);

    int update(LostPropertyAndRecruitDO lostPropertyAndRecruitDO);

    List<LostPropertyAndRecruitDO> listByJwcAccount(@Param("jwcAccount") String jwcAccount);
}
