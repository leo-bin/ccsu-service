package com.hang.service;

import com.hang.dao.AdviserDAO;
import com.hang.exceptions.ApiAssert;
import com.hang.pojo.data.AdviserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 导师服务层
 */
@Service
public class AdviserService {

    @Autowired
    private AdviserDAO adviserDAO;

   public List<AdviserDO> getAdvisers(int start, int offset){
       return adviserDAO.listAdviser(start,offset);
    }

   public AdviserDO getAdviser(int id){
       return adviserDAO.selectAdviserInfo(id);
   }

   public void updateAdviserInfo(AdviserDO adviserDo){
       int i=adviserDAO.updateAdviserInfo(adviserDo);
       ApiAssert.nonEqualInteger(i, 1, "更新失败");
   }

   public int insertAdviserInfo(AdviserDO adviserDo){
       int i=adviserDAO.insertAdviser(adviserDo);
       ApiAssert.nonEqualInteger(i, 1, "更新失败");
       return i;
   }


}
