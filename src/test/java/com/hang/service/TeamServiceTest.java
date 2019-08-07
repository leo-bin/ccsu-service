package com.hang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.hang.CcsuServiceApplicationTests;
import com.hang.dao.NotificationDAO;
import com.hang.dao.TeamDAO;
import com.hang.enums.NotificationEnum;
import com.hang.pojo.data.SystemNotificationDO;
import com.hang.pojo.data.TeamDO;
import com.hang.pojo.vo.GroupMemberVO;
import com.hang.pojo.vo.TeamVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hang.constant.InformationConstant.SYSTEM_NOTIFICATION_SUFFIX;
import static org.junit.Assert.*;

public class TeamServiceTest extends CcsuServiceApplicationTests {

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private TeamService teamService;

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private NotificationService notificationService;

    @Test
    public void createTeam() {
        TeamDO teamDO = new TeamDO();
        teamDO.setName("团队名字");
        teamDO.setAdvisor("老师名字");
        Integer id=teamDAO.insertTeam(teamDO);
        Integer teamId=teamDO.getId();
        String openId="dhyhdysgdsd";
        if (id>=1){
            GroupMemberVO groupMemberVO=new GroupMemberVO();
            groupMemberVO.setAvatar("ujhdddgdsdhs");
            groupMemberVO.setName("李斌");
            groupMemberVO.setTitle("17计科3班李斌");
            groupMemberVO.setRole("组长");
            teamService.addMember2Team(teamId,groupMemberVO,openId);
        }
        else{
            System.out.println("插入失败！");
        }
    }

    @Test
    public void  getTeamByUserId() {
        String userId="nishdhiudhsh";
        List<TeamDO> teamDOS = teamDAO.selectTeamByUserId(userId);
        List<TeamVO> teamVOS = teamDOS.stream().map(teamDO -> teamService.teamDO2VO(teamDO)).collect(Collectors.toList());
        System.out.println(teamVOS);
    }

    /**
     * 添加成员
     */
    @Test
    public void addMember2Team() {
        String openId="dhyhdysgdsd";
        String targetOpenId="nishdhiudhsh";
        int teamId=23;
        GroupMemberVO groupMemberVO=new GroupMemberVO();
        groupMemberVO.setAvatar("nsihsgskzjshs");
        groupMemberVO.setName("马涛");
        groupMemberVO.setTitle("17信科马涛");
        groupMemberVO.setRole("组员");
        TeamDO teamDO = teamDAO.selectByTeamId(teamId);
        String members = teamDO.getMembers();
        SystemNotificationDO systemNotificationDO=new SystemNotificationDO();
        ArrayList<GroupMemberVO> groupMemberVOS = JSON.parseObject(members, new TypeReference<ArrayList<GroupMemberVO>>() {
        });
        if(groupMemberVOS == null) {
            groupMemberVOS = Lists.newArrayList();
        }
        groupMemberVOS.add(groupMemberVO);
        teamDO.setMembers(JSON.toJSONString(groupMemberVOS));
        teamDAO.updateTeam(teamDO);
        //将成员和团队进行绑定
        teamDAO.insert2TeamUser(teamId, targetOpenId);
        //自己不能给自己发通知
        if (!openId.equals(targetOpenId)){
            systemNotificationDO.setNoteType(NotificationEnum.SYSTEM_NOTE_INVITATION.name());
            systemNotificationDO.setMessage(teamDO.getName()+SYSTEM_NOTIFICATION_SUFFIX);
            notificationDAO.insertSystemNote(systemNotificationDO);
            Integer notificationId=systemNotificationDO.getId();
            notificationService.sendNotification(openId,targetOpenId, NotificationEnum.SYSTEM_NOTE_INVITATION,notificationId,teamDO.getName()+SYSTEM_NOTIFICATION_SUFFIX,"");
        }
    }

}