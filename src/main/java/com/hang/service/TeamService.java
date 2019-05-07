package com.hang.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hang.dao.TeamDAO;
import com.hang.pojo.data.ProjectDO;
import com.hang.pojo.data.TeamDO;
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
    private ProjectService projectService;

    @Transactional(rollbackFor = Exception.class)
    public TeamVO createTeam(String name) {
        TeamDO teamDO = new TeamDO();
        teamDO.setName(name);
        teamDAO.insertTeam(teamDO);
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
        TeamDO teamDO = teamDAO.selectByTeamId(teamId);
        String members = teamDO.getMembers();
        ArrayList<GroupMemberVO> groupMemberVOS = JSON.parseObject(members, new TypeReference<ArrayList<GroupMemberVO>>() {
        });
        groupMemberVOS.add(groupMemberVO);
        teamDO.setMembers(JSON.toJSONString(groupMemberVOS));
        teamDAO.updateTeam(teamDO);
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
        TeamDO teamDO = teamDAO.selectByTeamId(teamId);
        String honors = teamDO.getHonor();
        if (honors != null && honors.contains(",")) {
            honors = honor;
        } else {
            honors += "," + honor;
        }
        teamDO.setHonor(honors);
        teamDAO.updateTeam(teamDO);
    }

    public void addLog2Team(int teamId, Date time, String log) {
        TeamDO teamDO = teamDAO.selectByTeamId(teamId);
        ArrayList<TeamLogVO> teamLogVOS;
        if (StringUtils.isBlank(teamDO.getLog())) {
            teamLogVOS = new ArrayList<>();
        } else {
            teamLogVOS = JSON.parseObject(teamDO.getLog(), new TypeReference<ArrayList<TeamLogVO>>() {});
        }

        teamLogVOS.add(new TeamLogVO(time, log));
        teamDO.setLog(JSON.toJSONString(teamLogVOS));
        teamDAO.updateTeam(teamDO);
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
        if (StringUtils.isNotBlank(teamDO.getLog())) {
            teamVO.setTeamLog(JSON.parseObject(teamDO.getLog(), new TypeReference<ArrayList<TeamLogVO>>() {}));
        }

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
        /// List<TeamDO> teamDOS = teamDAO.selectTeamByProjectId(projectPO.getId());
        /// Map<String, Integer> teams = teamDOS.stream().collect(Collectors.toMap(TeamDO::getName, TeamDO::getId));
        /// projectVO.setTeams(teams);

        String schedule = projectDO.getSchedule();
        if (StringUtils.isNotBlank(schedule)) {
            projectVO.setSchedule(JSON.parseObject(projectDO.getSchedule(), new TypeReference<ArrayList<ProjectScheduleVO>>() {}));
        }
        return projectVO;
    }

}
