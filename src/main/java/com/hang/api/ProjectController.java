package com.hang.api;

import com.hang.aop.StatisticsTime;
import com.hang.pojo.vo.BaseRes;
import com.hang.pojo.vo.ProjectVO;
import com.hang.service.ProjectService;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author hangs.zhang
 * @date 2019/1/25
 * *****************
 */
@Api("项目相关接口")
@RequestMapping("/project")
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @StatisticsTime("list")
    @GetMapping("/list")
    public BaseRes list() {
        return RespUtil.success(projectService.list());
    }

    /**
     * 根据projectId查询项目
     */
    @StatisticsTime("getProjectByProjectId")
    @ApiOperation("根据projectId获取项目信息")
    @GetMapping("/getProjectByProjectId")
    public BaseRes getProjectByProjectId(@RequestParam int projectId) {
        ProjectVO projectVO = projectService.getProjectByProjectId(projectId);
        return RespUtil.success(projectVO);
    }

    /**
     * 基本信息管理，即修改基本信息
     */
    @StatisticsTime("updateProject")
    @ApiOperation("更新项目基本信息")
    @RequestMapping("/updateProject")
    public BaseRes updateProject(@RequestParam Integer projectId, @RequestParam String name,
                                 @RequestParam String description, @RequestParam String properties) {
        projectService.updateProject(projectId, name, description, properties);
        return RespUtil.success();
    }

    /**
     * 增加honor
     */
    @StatisticsTime("addHonor2Project")
    @ApiOperation("为项目添加荣誉")
    @RequestMapping("/addHonor2Project")
    public BaseRes addHonor2Project(@RequestParam Integer projectId, String honors) {
        projectService.addHonor2Project(projectId, honors);
        return RespUtil.success();
    }

    /**
     * 追加最新进展
     */
    @StatisticsTime("addSchedule2Project")
    @ApiOperation("为项目追加进度")
    @RequestMapping("/addSchedule2Project")
    public BaseRes addSchedule2Project(@RequestParam Integer projectId,@RequestParam String schedules) {
        projectService.addSchedule2Project(projectId, schedules);
        return RespUtil.success();
    }

    /**
     * 制定项目甘特图
     */
    @StatisticsTime("addPlan2Project")
    @ApiOperation("为项目添加计划")
    @RequestMapping("/addPlan2Project")
    public BaseRes addPlan2Project(@RequestParam Integer projectId, @RequestParam String plans) {
        projectService.addPlan2Project(projectId, plans);
        return RespUtil.success();
    }
}
