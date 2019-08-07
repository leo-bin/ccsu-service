package com.hang.dao;


import com.hang.pojo.data.NotificationDO;
import com.hang.pojo.data.SystemNotificationDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author test
 */
@Repository
public interface NotificationDAO {

    int deleteByPrimaryKey(Integer id);

    int insert(NotificationDO record);

    int insertSelective(NotificationDO record);

    NotificationDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NotificationDO record);

    int updateByPrimaryKeyWithBLOBs(NotificationDO record);

    int updateByPrimaryKey(NotificationDO record);

    /**
     * 插入系统通知
     */
    int insertSystemNote(SystemNotificationDO systemNotificationDO);

    /**
     * 查评论等消息
     */
    List<Map> findCommentNoteByOpenId(
            @Param("targetOpenId") String targetOpenId,
            @Param("pageNo") Integer pageNo,
            @Param("pageSize") Integer pageSize,
            @Param("orderBy") String orderBy
    );

    /**
     * 查系统通知消息
     */
    List<Map> findSystemNoteByOpenId(
            @Param("targetOpenId") String targetOpenId,
            @Param("pageNo") Integer pageNo,
            @Param("pageSize") Integer pageSize,
            @Param("orderBy") String orderBy
    );

    int countByTargetOpenId(@Param("targetOpenId") String targetOpenId);

    void updateByIsRead(String targetOpenId);

    void deleteNotification(
            @Param("targetOpenId") String targetOpenId,
            @Param("userId") Integer userId,
            @Param("notificationId") Integer notificationId);
}