package com.hang.dao;

import com.hang.pojo.data.ProjectDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hangs.zhang
 * @date 2019/1/26
 * *****************
 * function:
 */
@Repository
public interface ProjectDAO {

    /**
     * 插入project
     *
     * @param projectPO
     * @return
     */
    int insertProject(ProjectDO projectPO);

    /**
     * 查询project
     *
     * @param id
     * @return
     */
    ProjectDO selectByProjectId(int id);

    /**
     * 更新
     *
     * @param projectPO
     * @return
     */
    int updateProject(ProjectDO projectPO);

    List<ProjectDO> list();
}
