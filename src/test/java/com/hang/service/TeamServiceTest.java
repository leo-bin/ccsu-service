package com.hang.service;

import com.hang.CcsuServiceApplicationTests;
import com.hang.dao.TeamDAO;
import com.hang.pojo.data.TeamDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class TeamServiceTest extends CcsuServiceApplicationTests {

    @Autowired
    private TeamDAO teamDAO;

    @Test
    public void createTeam() {
        TeamDO teamDO = new TeamDO();
        teamDO.setName("团队名字");
        teamDO.setAdvisor("老师名字");
        Integer id=teamDAO.insertTeam(teamDO);
        Integer teamId=teamDO.getId();
        String openId="dhyhdysgdsd";
        if (id>=1){
            System.out.println(teamId);
            teamDAO.insert2TeamUser(teamId,openId);
        }
        else{
            System.out.println("插入失败！");
        }
    }

}