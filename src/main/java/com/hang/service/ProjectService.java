package com.hang.service;

import com.hang.dao.ProjectDAO;
import com.hang.pojo.data.ProjectDO;
import com.hang.pojo.vo.ProjectVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public void addSchedule2Project(int projectId, String schedule) {
        ProjectDO projectPO = projectDAO.selectByProjectId(projectId);
        String schedules = projectPO.getSchedule();
        schedule = LocalDate.now().toString() + " " + schedule;
        if (StringUtils.isBlank(schedules)) {
            schedules = schedule;
        } else {
            schedules += "," + schedule;
        }
        projectPO.setSchedule(schedules);
        projectDAO.updateProject(projectPO);
    }

}
