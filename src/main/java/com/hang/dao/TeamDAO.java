package com.hang.dao;

import com.hang.pojo.data.ProjectDO;
import com.hang.pojo.data.TeamDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hangs.zhang
 * @date 2019/1/26
 * *****************
 * function:
 */
@Repository
public interface TeamDAO {

    /**
     * 插入team
     *
     * @param teamPO
     * @return
     */
    int insertTeam(TeamDO teamPO);

    /**
     * 插入team与user的映射
     *
     * @param teamId
     * @param userId
     * @return
     */
    int insert2TeamUser(@Param("teamId") int teamId, @Param("userId") String userId);

    /**
     * 插入project与team的映射
     *
     * @param teamId
     * @param projectId
     * @return
     */
    int insert2TeamProject(@Param("teamId") int teamId, @Param("projectId") int projectId);

    /**
     * 根据teamId查询team
     *
     * @param teamId
     * @return
     */
    TeamDO selectByTeamId(int teamId);

    /**
     * 更新team
     *
     * @param teamPO
     * @return
     */
    boolean updateTeam(TeamDO teamPO);

    /**
     * 删除team
     *
     * @param teamId
     * @return
     */
    boolean deleteTeamByTeamId(int teamId);

    /**
     * 查询team所属的project
     *
     * @param teamId
     * @return
     */
    List<ProjectDO> selectProjectByTeamId(int teamId);

    /**
     * 查询project依附team
     *
     * @param projectId
     * @return
     */
    List<TeamDO> selectTeamByProjectId(int projectId);

    /**
     * 查询user对应的团队
     *
     * @param userId
     * @return
     */
    List<TeamDO> selectTeamByUserId(String userId);

}
