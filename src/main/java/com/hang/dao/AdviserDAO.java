package com.hang.dao;

import com.hang.pojo.data.AdviserDO;
import com.hang.pojo.data.InformationDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LEO-BIN
 * @date 2019/6/20
 */
@Repository
public interface AdviserDAO {

    /**
     * 查询导师信息
     */
   AdviserDO selectAdviserInfo(int id);

    /**
     * 查询所有导师信息
     */
    List<AdviserDO> listAdviser(@Param("start") int start, @Param("offset") int offset);

    /**
     * 删除导师
     */
    int deleteAdviser(int id);

    /**
     * 修改导师信息
     */
    int updateAdviserInfo(AdviserDO adviserDo);

    /**
     * 增加导师信息
     */
    int insertAdviser(AdviserDO adviserDo);

}
