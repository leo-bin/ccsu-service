package com.hang.bbs.notification.mapper;


import com.hang.bbs.notification.pojo.Notification;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author test
 */
@Repository
public interface NotificationMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Notification record);

    int insertSelective(Notification record);

    Notification selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notification record);

    int updateByPrimaryKeyWithBLOBs(Notification record);

    int updateByPrimaryKey(Notification record);

    List<Map> findByTargetOpenId(
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
            @Param("topicId") Integer topicId);

}