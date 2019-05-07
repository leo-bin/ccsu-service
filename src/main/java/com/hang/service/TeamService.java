package com.hang.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hang.dao.TeamDAO;
import com.hang.pojo.data.ProjectDO;
import com.hang.pojo.data.TeamDO;
import com.hang.pojo.vo.GroupMemberVO;
import com.hang.pojo.vo.ProjectVO;
import com.hang.pojo.vo.TeamVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private ProjectService projectService;

    @Transactional(rollbackFor = Exception.class)
    public TeamVO createTeam(String name) {
        TeamDO teamDO = new TeamDO();
        teamDO.setName(name);
        teamDAO.insertTeam(teamDO);
        return teamDO2VO(teamDO);
    }

    public TeamVO getTeamByTeamId(int teamId) {
        TeamDO teamPO = teamDAO.selectByTeamId(teamId);
        TeamVO teamVO = teamDO2VO(teamPO);
        return teamVO;
    }

    public List<TeamVO> getTeamByUserId(String userId) {
        List<TeamDO> teamDOS = teamDAO.selectTeamByUserId(userId);
        List<TeamVO> teamVOS = teamDOS.stream().map(teamPO -> teamDO2VO(teamPO)).collect(Collectors.toList());
        return teamVOS;
    }

    public List<TeamVO> getTeams(int start, int offset) {
        List<TeamDO> teamDOS = teamDAO.selectTeams(start, offset);
        return teamDOS.stream().map(teamPO -> teamDO2VO(teamPO)).collect(Collectors.toList());
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
    public void addMember2Team(int teamId, GroupMemberVO groupMemberVO) {
        TeamDO teamPO = teamDAO.selectByTeamId(teamId);
        String members = teamPO.getMembers();
        ArrayList<GroupMemberVO> groupMemberVOS = JSON.parseObject(members, new TypeReference<ArrayList<GroupMemberVO>>() {
        });
        groupMemberVOS.add(groupMemberVO);
        teamPO.setMembers(JSON.toJSONString(groupMemberVOS));
        teamDAO.updateTeam(teamPO);
    }

    /**
     * 添加userId到team
     *
     * @param teamId
     * @param openId
     */
    public void addUser2Team(int teamId, String openId) {
        teamDAO.insert2TeamUser(teamId, openId);
    }

    /**
     * 添加项目到team
     *
     * @param teamId
     * @param projectPO
     */
    public void addProject2Team(int teamId, ProjectDO projectPO) {
        ProjectDO project = projectService.addProject(projectPO);
        teamDAO.insert2TeamProject(teamId, project.getId());
    }

    public void addHonor2Team(int teamId, String honor) {
        TeamDO teamPO = teamDAO.selectByTeamId(teamId);
        String honors = teamPO.getHonor();
        if (honors != null && honors.contains(",")) {
            honors = honor;
        } else {
            honors += "," + honor;
        }
        teamPO.setHonor(honors);
        teamDAO.updateTeam(teamPO);
    }

    public void addLog2Team(int teamId, String log) {
        TeamDO teamPO = teamDAO.selectByTeamId(teamId);
        LocalDate localDate = LocalDate.now();
        log = localDate.toString() + " " + log;
        if (teamPO.getLog() != null && teamPO.getLog().length() > 0) {
            log = teamPO.getLog() + "," + log;
        }
        teamPO.setLog(log);
        teamDAO.updateTeam(teamPO);
    }

    private TeamVO teamDO2VO(TeamDO teamDO) {
        TeamVO teamVO = new TeamVO();
        ArrayList<GroupMemberVO> groupMemberVOS = JSON.parseObject(teamDO.getMembers(), new TypeReference<ArrayList<GroupMemberVO>>() {
        });
        teamVO.setGroupMemberVOS(groupMemberVOS);
        teamVO.setId(teamDO.getId());
        teamVO.setName(teamDO.getName());
        teamVO.setAdvisor(teamDO.getAdvisor());
        teamVO.setAvatar(teamDO.getAvatar());

        String honor = teamDO.getHonor();
        if (StringUtils.isNotBlank(honor)) {
            String[] split = honor.split(",");
            teamVO.setHonor(Arrays.asList(split));
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

    public ProjectVO projectPO2VO(ProjectDO projectPO) {
        ProjectVO projectVO = new ProjectVO();
        projectVO.setId(projectPO.getId());
        projectVO.setName(projectPO.getName());
        projectVO.setDescription(projectPO.getDescription());
        if (StringUtils.isNotBlank(projectPO.getHonor())) {
            projectVO.setHonors(Arrays.asList(projectPO.getHonor().split(",")));
        }
        if (StringUtils.isNotBlank(projectPO.getProperties())) {
            projectVO.setProperties(projectPO.getProperties());
        }
        List<TeamDO> teamDOS = teamDAO.selectTeamByProjectId(projectPO.getId());
        Map<String, Integer> teams = teamDOS.stream().collect(Collectors.toMap(TeamDO::getName, TeamDO::getId));
        projectVO.setTeams(teams);

        String schedule = projectPO.getSchedule();
        if (StringUtils.isNotBlank(schedule)) {
            projectVO.setSchedule(Arrays.asList(schedule.split(",")));
        }
        return projectVO;
    }

}
