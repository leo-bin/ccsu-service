package com.hang.dao;

import com.hang.CcsuServiceApplicationTests;
import com.hang.enums.LostPropertyAndRecruitEnum;
import com.hang.pojo.data.LostPropertyAndRecruitDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function:
 */
public class LostPropertyAndRecruitDAOTest extends CcsuServiceApplicationTests {

    @Autowired
    private LostPropertyAndRecruitDAO lostPropertyAndRecruitDAO;

    @Test
    public void insert2() throws Exception {
        LostPropertyAndRecruitDO lostPropertyAndRecruitDO = new LostPropertyAndRecruitDO();
        lostPropertyAndRecruitDO.setInitiatorName("张航");
        lostPropertyAndRecruitDO.setCategory(" RECRUIT");
        lostPropertyAndRecruitDO.setInitiatorJwcAccount("B20150304203");
        lostPropertyAndRecruitDO.setContactInformation("18374976843");
        lostPropertyAndRecruitDO.setInitiatorMessage("this is test message 4");
        lostPropertyAndRecruitDO.setInitiatorLocation("致远楼");
        String dateStr = "2018-08-12";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        lostPropertyAndRecruitDO.setOccurTime(date);
        //lostPropertyAndRecruitDO.setOccurTime(new Date());
        lostPropertyAndRecruitDO.setDatetime(new Date());
        lostPropertyAndRecruitDAO.insert(lostPropertyAndRecruitDO);
    }

    @Test
    public void list() throws Exception {
        List<LostPropertyAndRecruitDO> lostPropertyAndRecruitDOS = lostPropertyAndRecruitDAO.listAll(0, 10);
        lostPropertyAndRecruitDOS.forEach(System.out::println);
    }

    @Test
    public void listByCategory() throws Exception {
        List<LostPropertyAndRecruitDO> lostPropertyAndRecruitDOS = lostPropertyAndRecruitDAO.listByCategory(LostPropertyAndRecruitEnum.LOSTPROPERTY.name(), 0, 10);
        lostPropertyAndRecruitDOS.forEach(System.out::println);
        System.out.println("********");
        List<LostPropertyAndRecruitDO> lostPropertyAndRecruitDOS2 = lostPropertyAndRecruitDAO.listByCategory(LostPropertyAndRecruitEnum.RECRUIT.name(), 0, 10);
        lostPropertyAndRecruitDOS2.forEach(System.out::println);
    }

    @Test
    public void delete() throws Exception{
        lostPropertyAndRecruitDAO.delete(19);

    }

    @Test
    public void testjwc() throws Exception {
        List<LostPropertyAndRecruitDO> lostPropertyAndRecruitDOS = lostPropertyAndRecruitDAO.listByJwcAccount("B20150304203");
        lostPropertyAndRecruitDOS.forEach(System.out::println);
    }
}