package com.hang.dao;

import com.hang.pojo.data.InformationApplyDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hangs.zhang
 * @date 2019/1/30
 * *****************
 * function: 活动申请的dao
 */
@Repository
public interface ApplyDAO {

    InformationApplyDO selectById(int id);

    List<InformationApplyDO> selectByOpenId(String openId);

    int insert(InformationApplyDO informationApply);

    int update(@Param("applyId") int applyId, @Param("status") String status);

    List<InformationApplyDO> listAppliesByInformationId(int informationId);

    List<InformationApplyDO> list(@Param("start") int start, @Param("offset") int offset);

}
