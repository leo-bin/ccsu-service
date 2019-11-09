package com.hang.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.hang.dao.StudentDAO;
import com.hang.dao.TeamDAO;
import com.hang.pojo.data.*;
import com.hang.pojo.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hangs.zhang
 * @date 2019/1/26
 * *****************
 * function:
 */
@Service
public class TeamService {

    @Autowired
    private TeamDAO teamDAO;

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private ProjectService projectService;


    @Transactional(rollbackFor = Exception.class)
    public TeamVO createTeam(String openId,String name, String advisor) {
        TeamDO teamDO = new TeamDO();
        teamDO.setName(name);
        teamDO.setAdvisor(advisor);
        Integer id=teamDAO.insertTeam(teamDO);
        //获取插入数据库后的teamId
        Integer teamId=teamDO.getId();
        StudentDO studentDO=studentDAO.selectStudentDOByOpenId(openId);
        //teamId和openId进行绑定
        if (id>=1&&studentDO!=null){
            GroupMemberVO groupMemberVO=new GroupMemberVO();
            groupMemberVO.setAvatar(studentDO.getAvatar());
            groupMemberVO.setName(studentDO.getRealName());
            groupMemberVO.setTitle(studentDO.getTitle());
            groupMemberVO.setRole("组长");
            addMember2Team(teamId,groupMemberVO,openId);
        }
        return teamDO2VO(teamDO);
    }

    public TeamVO getTeamByTeamId(int teamId) {
        TeamDO teamDO = teamDAO.selectByTeamId(teamId);
        TeamVO teamVO = teamDO2VO(teamDO);
        return teamVO;
    }

    public List<TeamVO> getTeamByUserId(String userId) {
        List<TeamDO> teamDOS = teamDAO.selectTeamByUserId(userId);
        List<TeamVO> teamVOS = teamDOS.stream().map(teamDO -> teamDO2VO(teamDO)).collect(Collectors.toList());
        return teamVOS;
    }

    public List<TeamVO> getTeams(int start, int offset) {
        List<TeamDO> teamDOS = teamDAO.selectTeams(start, offset);
        return teamDOS.stream().map(teamDO -> teamDO2VO(teamDO)).collect(Collectors.toList());
    }

    /**
     * 根据导师姓名获取团队
     */
    public List<TeamVO> getTeamsByAdvisor(String advisor){
        List<TeamDO> teamDOS=teamDAO.selectTeamsByAdvisor(advisor);
        return teamDOS.stream().map(teamDO -> teamDO2VO(teamDO)).collect(Collectors.toList());
    }

    /**
     * 添加team头像
     */
    public void addAvatar2Team(Integer teamId, String avatarUrl) {
        TeamDO teamDO = new TeamDO();
        teamDO.setId(teamId);
        teamDO.setAvatar(avatarUrl);
        teamDAO.updateTeam(teamDO);
    }

    /**
     * 添加成员
     *
     * @param teamId
     * @param groupMemberVO
     */
    public void addMember2Team(int teamId, GroupMemberVO groupMemberVO,String targetOpenId) {
        TeamDO teamDO = teamDAO.selectByTeamId(teamId);
        String members = teamDO.getMembers();
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
    }

    /**
     * 添加项目到team
     */
    public void addProject2Team(int teamId, ProjectDO projectPO) {
        ProjectDO project = projectService.addProject(projectPO);
        teamDAO.insert2TeamProject(teamId, project.getId());
    }


    /**
     * 添加荣誉到team
     */
    public void addHonor2Team(int teamId, String honors) {
        TeamDO teamDO = teamDAO.selectByTeamId(teamId);
        JSONArray jsonArray=JSONArray.parseArray(honors);
        ArrayList<HonorVO> teamHonors =Lists.newArrayList();
        for (Object jsonObject:jsonArray){
            HonorVO honorVO =JSON.parseObject(jsonObject.toString(), HonorVO.class);
            teamHonors.add(honorVO);
        }
        teamDO.setHonor(JSON.toJSONString(teamHonors));
        teamDAO.updateTeam(teamDO);
    }

    /**
     * 添加团队日志到team
     */
    public void addLog2Team(int teamId,String logs) {
        TeamDO teamDO = teamDAO.selectByTeamId(teamId);
        JSONArray jsonArray=JSONArray.parseArray(logs);
        ArrayList<TeamLogVO> teamLogVOS=Lists.newArrayList();
        for (Object jsonObject:jsonArray){
            TeamLogVO teamLogVO=JSON.parseObject(jsonObject.toString(),TeamLogVO.class);
            teamLogVOS.add(teamLogVO);
        }
        teamDO.setLog(JSON.toJSONString(teamLogVOS));
        teamDAO.updateTeam(teamDO);
    }

    /**
     * 团队实体的DO到VO的转换
     * @param teamDO
     * @return
     */
    public TeamVO teamDO2VO(TeamDO teamDO) {
        TeamVO teamVO = new TeamVO();
        //反序列化
        ArrayList<GroupMemberVO> groupMemberVOS = JSON.parseObject(teamDO.getMembers(), new TypeReference<ArrayList<GroupMemberVO>>() {
        });
        teamVO.setGroupMemberVOS(groupMemberVOS);
        teamVO.setId(teamDO.getId());
        teamVO.setName(teamDO.getName());
        teamVO.setAdvisor(teamDO.getAdvisor());
        teamVO.setAvatar(teamDO.getAvatar());
        teamVO.setState(teamDO.getState());
        if (StringUtils.isNotBlank(teamDO.getLog())) {
            teamVO.setTeamLog(JSON.parseObject(teamDO.getLog(), new TypeReference<ArrayList<TeamLogVO>>() {
            }));
        }
        if (StringUtils.isNotBlank(teamDO.getHonor())) {
            teamVO.setHonor(JSON.parseObject(teamDO.getHonor(), new TypeReference<List<HonorVO>>() {
            }));
        }
        if (!Objects.isNull(teamDO.getId())) {
            List<ProjectDO> projectPOS = teamDAO.selectProjectByTeamId(teamDO.getId());
            if (!Objects.isNull(projectPOS)) {
                List<ProjectVO> projects = projectPOS.stream().map(projectPO -> projectPO2VO(projectPO)).collect(Collectors.toList());
                teamVO.setProjects(projects);
            }
        }
        return teamVO;
    }

    /**
     * 项目的DO到VO的转换
     * @param projectDO
     * @return
     */
    public ProjectVO projectPO2VO(ProjectDO projectDO) {
        ProjectVO projectVO = new ProjectVO();
        projectVO.setId(projectDO.getId());
        projectVO.setName(projectDO.getName());
        projectVO.setDescription(projectDO.getDescription());
        projectVO.setDetailDescription(projectDO.getDetailDescription());
        if (StringUtils.isNotBlank(projectDO.getHonor())) {
            projectVO.setHonors(Arrays.asList(projectDO.getHonor().split(",")));
        }
        if (StringUtils.isNotBlank(projectDO.getProperties())) {
            projectVO.setProperties(projectDO.getProperties());
        }
        String schedule = projectDO.getSchedule();
        String plans = projectDO.getProjectPlan();
        if (StringUtils.isNotBlank(schedule)) {
            projectVO.setSchedule(JSON.parseObject(projectDO.getSchedule(), new TypeReference<ArrayList<ProjectScheduleVO>>() {
            }));
        }
        if (StringUtils.isNotBlank(plans)) {
            projectVO.setProjectPlan(JSON.parseObject(projectDO.getProjectPlan(), new TypeReference<ArrayList<ProjectPlanVO>>() {
            }));
        }
        return projectVO;
    }


    /**
     * 修改团队信息
     */
    public Boolean updateTeamInfo(TeamDO teamDO){
        return teamDAO.updateTeam(teamDO);
    }

    /**
     * 完善团队成员信息
     */
    public void updateTeamMemberInfo(String realName, String title, String openId) {
        StudentDO studentDO = studentDAO.selectStudentDOByOpenId(openId);
        studentDO.setRealName(realName);
        studentDO.setTitle(title);
        studentDAO.updateStudentInfo(studentDO);
    }
}
