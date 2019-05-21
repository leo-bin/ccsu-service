package com.hang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.hang.dao.ProjectDAO;
import com.hang.pojo.data.ProjectDO;
import com.hang.pojo.vo.ProjectScheduleVO;
import com.hang.pojo.vo.ProjectVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    public void addHonor2Project(int projectId, String honor) {
        ProjectDO projectPO = projectDAO.selectByProjectId(projectId);
        String honors = projectPO.getHonor();
        if (StringUtils.isBlank(honors)) {
            honors = honor;
        } else {
            honors += "," + honor;
        }
        projectPO.setHonor(honors);
        projectDAO.updateProject(projectPO);
    }

    public void addSchedule2Project(int projectId, Date time, String schedule) {
        ProjectDO projectDO = projectDAO.selectByProjectId(projectId);
        List<ProjectScheduleVO> projectScheduleVOS;
        if (StringUtils.isNotBlank(projectDO.getSchedule())) {
            projectScheduleVOS = JSON.parseObject(projectDO.getSchedule(), new TypeReference<ArrayList<ProjectScheduleVO>>() {});
        } else {
            projectScheduleVOS = Lists.newArrayList();
        }

        projectScheduleVOS.add(new ProjectScheduleVO(time, schedule));
        projectDO.setSchedule(JSON.toJSONString(projectScheduleVOS));
        projectDAO.updateProject(projectDO);
    }

}
