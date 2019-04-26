package com.hang.dao;

import com.hang.pojo.data.InformationDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


/**
 * @author hangs.zhang
 * @date 2019/1/28
 * *****************
 * function:
 */
@Repository
public interface InformationDAO {

    int insert(InformationDO information);

    int delete(int id);

    int update(InformationDO information);

    InformationDO selectById(int id);

    ArrayList<InformationDO> list(@Param("start") int start, @Param("offset") int offset);

    ArrayList<InformationDO> listByCategory(@Param("category") String category, @Param("start") int start,
                                            @Param("offset") int offset);

    int count();

    int maxId();

}
