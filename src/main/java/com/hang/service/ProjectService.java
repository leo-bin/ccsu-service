package com.hang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.hang.dao.ProjectDAO;
import com.hang.pojo.data.ProjectDO;
import com.hang.pojo.vo.ProjectPlanVO;
import com.hang.pojo.vo.ProjectScheduleVO;
import com.hang.pojo.vo.ProjectVO;
import com.hang.pojo.vo.HonorVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hangs.zhang
 * @date 2019/1/27
 * *****************
 * function:
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private TeamService teamService;

    public List<ProjectDO> list() {
        return projectDAO.list();
    }

    public ProjectDO addProject(ProjectDO projectPO) {
        projectDAO.insertProject(projectPO);
        return projectPO;
    }

    public ProjectVO getProjectByProjectId(int projectId) {
        ProjectDO projectPO = projectDAO.selectByProjectId(projectId);
        return teamService.projectPO2VO(projectPO);
    }

    public void updateProject(int projectId, String name, String description, String properties) {
        ProjectDO projectPO = projectDAO.selectByProjectId(projectId);
        if (StringUtils.isNotBlank(name)) {
            projectPO.setName(name);
        }
        if (StringUtils.isNotBlank(description)) {
            projectPO.setDescription(description);
        }
        if (StringUtils.isNotBlank(properties)) {
            projectPO.setProperties(properties);
        }
        projectDAO.updateProject(projectPO);
    }

    public void addHonor2Project(int projectId, String honors) {
        ProjectDO projectPO = projectDAO.selectByProjectId(projectId);
        ArrayList<HonorVO> projectHonors=Lists.newArrayList();
        JSONArray jsonArray=JSONArray.parseArray(honors);
        for (Object jsonObject:jsonArray){
            HonorVO honor =JSON.parseObject(jsonObject.toString(), HonorVO.class);
            projectHonors.add(honor);
        }
        projectPO.setHonor(JSON.toJSONString(projectHonors));
        projectDAO.updateProject(projectPO);
    }

    public void addSchedule2Project(int projectId, String schedules) {
        ProjectDO projectDO = projectDAO.selectByProjectId(projectId);
        JSONArray jsonArray=JSONArray.parseArray(schedules);
        List<ProjectScheduleVO> projectScheduleVOS=Lists.newArrayList();
        for (Object jsonObject:jsonArray){
            ProjectScheduleVO projectScheduleVO=JSON.parseObject(jsonObject.toString(),ProjectScheduleVO.class);
            projectScheduleVOS.add(projectScheduleVO);
        }
        projectDO.setSchedule(JSON.toJSONString(projectScheduleVOS));
        projectDAO.updateProject(projectDO);
    }

    public void addPlan2Project(int projectId,String plans){
        ProjectDO projectDO=projectDAO.selectByProjectId(projectId);
        JSONArray jsonArray=JSONArray.parseArray(plans);
        List<ProjectPlanVO> projectPlanVOS=Lists.newArrayList();
        for (Object jsonObject:jsonArray){
            ProjectPlanVO projectPlanVO=JSON.parseObject(jsonObject.toString(),ProjectPlanVO.class);
            projectPlanVOS.add(projectPlanVO);
        }
        projectDO.setProjectPlan(JSON.toJSONString(projectPlanVOS));
        projectDAO.updateProject(projectDO);
    }
}
