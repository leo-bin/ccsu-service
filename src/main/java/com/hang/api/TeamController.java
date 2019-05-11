package com.hang.api;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hang.annotation.OpenId;
import com.hang.aop.StatisticsTime;
import com.hang.enums.ResultEnum;
import com.hang.exceptions.ApiAssert;
import com.hang.pojo.data.ProjectDO;
import com.hang.pojo.vo.BaseRes;
import com.hang.pojo.vo.GroupMemberVO;
import com.hang.pojo.vo.ProjectVO;
import com.hang.pojo.vo.TeamVO;
import com.hang.service.TeamService;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author hangs.zhang
 * @date 2019/1/25
 * *****************
 * function:
 */
@Slf4j
@Api("双创团队接口")
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @ApiOperation("创建团队")
    @StatisticsTime("createTeam")
    @GetMapping("/createTeam")
    public BaseRes createTeam(@RequestParam String name) {

        teamService.createTeam(name);
        return RespUtil.success();
    }

    /**
     * 无差别获取team
     *
     * @return
     */
    @StatisticsTime("getAllTeam")
    @ApiOperation("无差别获取团队信息")
    @GetMapping("/getAllTeam")
    public BaseRes getAllTeam(@RequestParam(required = false, defaultValue = "0") int start,
                              @RequestParam(required = false, defaultValue = "10") int offset) {
        return RespUtil.success(teamService.getTeams(start, offset));
    }

    /**
     * 根据openId返回teamList，projectList
     *
     * @param openId
     * @return
     */
    @StatisticsTime("getTeamHomePage")
    @ApiOperation("获取用户自己的团队以及项目信息")
    @GetMapping("/getTeamHomePage")
    public BaseRes getTeamHomePage(@OpenId String openId) {
        log.info("checkOpenId : {}", openId);
        ApiAssert.checkOpenId(openId);

        HashMap<String, Object> result = Maps.newHashMap();
        List<TeamVO> teams = teamService.getTeamByUserId(openId);
        result.put("teams", teams);
        Set<ProjectVO> projects = Sets.newHashSet();
        teams.forEach(e -> projects.addAll(e.getProjects()));
        result.put("projects", projects);
        return RespUtil.success(result);
    }

    /**
     * 根据openId查询团队数据
     *
     * @param openId
     * @return
     */
    @StatisticsTime("getTeamByUserId")
    @ApiOperation("查询用户所属的团队")
    @GetMapping("/getTeamByUserId")
    public BaseRes getTeamByUserId(@OpenId String openId) {
        ApiAssert.checkOpenId(openId);
        List<TeamVO> teamVOS = teamService.getTeamByUserId(openId);
        return RespUtil.success(teamVOS);
    }

    /**
     * 根据teamId查询团队数据
     *
     * @param teamId
     * @return
     */
    @StatisticsTime("getTeamByTeamId")
    @ApiOperation("根据teamId查询team")
    @GetMapping("/getTeamByTeamId")
    public BaseRes getTeamByTeamId(@RequestParam int teamId) {
        TeamVO teamById = teamService.getTeamByTeamId(teamId);
        return RespUtil.success(teamById);
    }

    @StatisticsTime("addAvatar2Team")
    @ApiOperation("为team添加头像")
    @GetMapping("/addAvatar2Team")
    public BaseRes addAvatar2Team(@RequestParam Integer teamId, @RequestParam String avatar) {
        teamService.addAvatar2Team(teamId, avatar);
        return RespUtil.success();
    }

    /**
     * 增加成员
     *
     * @param teamId
     * @param groupMemberVO
     * @return
     */
    @StatisticsTime("addMember2Team")
    @ApiOperation("team添加成员")
    @GetMapping("/addMember2Team")
    public BaseRes addMember2Team(@RequestParam int teamId, @ModelAttribute GroupMemberVO groupMemberVO) {
        teamService.addMember2Team(teamId, groupMemberVO);
        return RespUtil.success();
    }

    /**
     * 增加wxUser到team
     *
     * @param teamId
     * @param openId
     * @return
     */
    @StatisticsTime("addWxUser2Team")
    @ApiOperation("项目添加微信成员")
    @GetMapping("/addUser2Team")
    public BaseRes addUser2Team(@RequestParam int teamId, @OpenId String openId) {
        ApiAssert.checkOpenId(openId);
        teamService.addUser2Team(teamId, openId);
        return RespUtil.success();
    }

    /**
     * 创建project，然后添加到team
     *
     * @param teamId
     * @param projectName
     * @param projectDescription
     * @return
     */
    @StatisticsTime("addProject2Team")
    @ApiOperation("为team添加项目")
    @GetMapping("/addProject2Team")
    public BaseRes addProject2Team(@RequestParam int teamId, @RequestParam String projectName,
                                   @RequestParam String projectDescription) {
        ProjectDO projectDO = new ProjectDO();
        projectDO.setDescription(projectDescription);
        projectDO.setName(projectName);
        teamService.addProject2Team(teamId, projectDO);
        return RespUtil.success();
    }

    /**
     * 增加荣誉
     *
     * @param teamId
     * @param honor
     * @return
     */
    @StatisticsTime("addHonor2Team")
    @ApiOperation("team添加荣耀")
    @GetMapping("/addHonor2Team")
    public BaseRes addHonor2Team(@RequestParam int teamId, @RequestParam String honor) {
        teamService.addHonor2Team(teamId, honor);
        return RespUtil.success();
    }

    /**
     * 追加日志
     *
     * @param teamId
     * @param log
     * @return
     */
    @StatisticsTime("addLog2Team")
    @ApiOperation("team添加日志")
    @GetMapping("/addLog2Team")
    public BaseRes addLog2Team(@RequestParam int teamId, @RequestParam Long time, @RequestParam String log) {
        Date date = new Date(time);
        teamService.addLog2Team(teamId, date, log);
        return RespUtil.success();
    }


}
