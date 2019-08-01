package com.hang.api;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hang.annotation.OpenId;
import com.hang.aop.StatisticsTime;
import com.hang.dao.NotificationDAO;
import com.hang.dao.TeamDAO;
import com.hang.enums.NotificationEnum;
import com.hang.enums.ResultEnum;
import com.hang.exceptions.ApiAssert;
import com.hang.pojo.data.*;
import com.hang.pojo.vo.BaseRes;
import com.hang.pojo.vo.GroupMemberVO;
import com.hang.pojo.vo.ProjectVO;
import com.hang.pojo.vo.TeamVO;
import com.hang.service.*;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.hang.constant.InformationConstant.INVITATION_SUCCESS_SUFFIX;
import static com.hang.constant.InformationConstant.SYSTEM_NOTIFICATION_SUFFIX;

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
    private TeamDAO teamDAO;

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private TeamService teamService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @ApiOperation("创建团队")
    @StatisticsTime("createTeam")
    @PostMapping("/createTeam")
    public BaseRes createTeam(@OpenId String openId,@RequestParam String name, @RequestParam String advisor) {
        ApiAssert.checkOpenId(openId);
        UserInfoDO userInfoDO=userService.getUserInfoByOpenId(openId);
        if (userInfoDO.getRoleId().equals(2)){
            teamService.createTeam(openId,name, advisor);
            return RespUtil.success();
        }
        return RespUtil.error(ResultEnum.AUTHORIZE_ERROR);
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
    @StatisticsTime("getMyTeam")
    @ApiOperation("获取用户自己的团队以及项目信息")
    @GetMapping("/getMyTeam")
    public BaseRes getMyTeam(@OpenId String openId) {
        log.info("checkOpenId : {}", openId);
        ApiAssert.checkOpenId(openId);
        UserInfoDO userInfoDO=userService.getUserInfoByOpenId(openId);
        Integer roleId=userInfoDO.getRoleId();
        HashMap<String, Object> result = Maps.newHashMap();
        if (roleId.equals(1)){
            TeacherDO teacherDO=teacherService.getTeacherInfo(openId);
            List<TeamVO> teams=teamService.getTeamsByAdvisor(teacherDO.getName());
            result.put("teams", teams);
            Set<ProjectVO> projects = Sets.newHashSet();
            teams.forEach(e -> projects.addAll(e.getProjects()));
            result.put("projects", projects);
        }
        else{
            List<TeamVO> teams = teamService.getTeamByUserId(openId);
            result.put("teams",teams);
            Set<ProjectVO> projects = Sets.newHashSet();
            teams.forEach(e1 -> projects.addAll(e1.getProjects()));
            result.put("projects", projects);
        }
        return RespUtil.success(result);
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

    /**
     * 为团队添加头像
     * @param teamId
     * @param avatar
     * @return
     */
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
     * @param jwcAccount
     * @return
     * @apiNote @ModelAttribute: 数据绑定，将url对应的请求参数和对象进行一一对应的绑定
     */
    @StatisticsTime("addMember2Team")
    @ApiOperation("team添加成员")
    @GetMapping("/addMember2Team")
    public BaseRes addMember2Team(@OpenId String openId,@RequestParam int teamId, @RequestParam String jwcAccount) {
        StudentDO studentDO=studentService.getStudentInfoByJwcAccount(jwcAccount);
        GroupMemberVO groupMemberVO=new GroupMemberVO();
        SystemNotificationDO systemNotificationDO=new SystemNotificationDO();
        groupMemberVO.setAvatar(studentDO.getAvatar());
        groupMemberVO.setName(studentDO.getRealName());
        groupMemberVO.setTitle(studentDO.getTitle());
        groupMemberVO.setRole("组员");
        teamService.addMember2Team(openId,teamId, groupMemberVO,studentDO.getOpenId());
        //邀请成功之后给邀请人发通知
        systemNotificationDO.setNoteType(NotificationEnum.SYSTEM_NOTE_INVITATION.name());
        systemNotificationDO.setMessage(INVITATION_SUCCESS_SUFFIX);
        notificationDAO.insertSystemNote(systemNotificationDO);
        Integer notificationId=systemNotificationDO.getId();
        notificationService.sendNotification(studentDO.getOpenId(),openId, NotificationEnum.SYSTEM_NOTE_INVITATION,notificationId,INVITATION_SUCCESS_SUFFIX);
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
                                   @RequestParam String projectDescription, @RequestParam String properties) {
        ProjectDO projectDO = new ProjectDO();
        projectDO.setDescription(projectDescription);
        projectDO.setName(projectName);
        projectDO.setProperties(properties);
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

    /**
     * 修改团队信息
     */
    @StatisticsTime("updateTeamInfo")
    @ApiOperation("修改团队信息")
    @PostMapping("/updateTeamInfo")
    public BaseRes updateTeamInfo(@RequestParam Integer id,
                                  @RequestParam String name,
                                  @RequestParam String advisor,
                                  @RequestParam Integer state){
        TeamVO teamVO=teamService.getTeamByTeamId(id);
        TeamDO teamDO=new TeamDO();
        teamDO.setId(id);
        teamDO.setName(name);
        teamDO.setAdvisor(advisor);
        teamDO.setState(state);
        if (teamVO!=null){
            return RespUtil.success(teamService.updateTeamInfo(teamDO));
        }
        else{
            return RespUtil.error(ResultEnum.TEAM_NOT_EXIT);
        }
    }

    /**
     * 修改团队成员的信息
     */
    @StatisticsTime("updateTeamInfo")
    @ApiOperation("修改团队信息")
    @PostMapping("/updateTeamMemberInfo")
    public BaseRes updateTeamMemberInfo(@OpenId String openId, @RequestParam Integer teamId,
                                        @RequestParam String realName, @RequestParam String title) {
        ApiAssert.checkOpenId(openId);
        TeamDO teamDO = teamDAO.selectByTeamId(teamId);
        teamService.updateTeamMemberInfo(realName, title, teamDO, openId);
        return RespUtil.success();
    }

    /**
     * 根据学号查询学生信息
     */
    @StatisticsTime("getStudentInfoByJwcAccount")
    @ApiOperation("根据学号查询")
    @PostMapping("/getStudentInfoByJwcAccount")
    public BaseRes getStudentInfoByJwcAccount(@RequestParam String jwcAccount){
        return RespUtil.success(studentService.getStudentInfoByJwcAccount(jwcAccount));
    }
}
