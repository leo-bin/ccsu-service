package com.hang.service;

import com.hang.CcsuServiceApplicationTests;
import com.hang.dao.InformationDAO;
import com.hang.pojo.data.InformationDO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;



public class InformationServiceTest extends CcsuServiceApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(InformationServiceTest.class);

    @Autowired
    private InformationDAO informationDAO;

    @Test
    public void getLatestInformation() {
        ArrayList<InformationDO> list = informationDAO.listNoteAndRecruitment(0,10);
        System.out.println(list);
    }
}