package com.hang.bbs.notification.service;


import com.hang.bbs.common.Page;
import com.hang.bbs.notification.mapper.NotificationMapper;
import com.hang.bbs.notification.pojo.Notification;
import com.hang.bbs.notification.pojo.NotificationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhanghang
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 保存通知
     *
     * @param notification
     */
    public void save(Notification notification) {
        notificationMapper.insertSelective(notification);
    }

    /**
     * 发送通知
     *
     * @param action
     * @param topicId
     * @param content
     */
    public void sendNotification(String openId, String targetOpenId, NotificationEnum action, Integer topicId, String content) {
        Notification notification = new Notification();
        notification.setOpenId(openId);
        notification.setTargetOpenId(targetOpenId);
        notification.setInTime(new Date());
        notification.setTopicId(topicId);
        notification.setAction(action.name());
        notification.setContent(content);
        notification.setIsRead(false);
        save(notification);
    }

    /**
     * 根据用户查询通知
     *
     * @param isRead
     * @return
     */
    public Page<Map> findByTargetUserAndIsRead(Integer pageNo, Integer pageSize, String targetOpenId, Boolean isRead) {
        List<Map> list = notificationMapper.findByTargetOpenId(targetOpenId, isRead, (pageNo - 1) * pageSize, pageSize, "n.is_read asc, n.id desc");
        int count = notificationMapper.countByTargetOpenId(targetOpenId, isRead);
        return new Page<>(pageNo, pageSize, count, list);
    }

    /**
     * 根据用户查询已读/未读的通知
     *
     * @param isRead
     * @return
     */
    public long countByTargetUserAndIsRead(String targetOpenId, boolean isRead) {
        return notificationMapper.countByTargetOpenId(targetOpenId, isRead);
    }

    /**
     * 批量更新通知的状态
     *
     */
    public void updateByIsRead(String targetOpenId) {
        notificationMapper.updateByIsRead(targetOpenId);
    }

    /**
     * 删除目标用户的通知
     *
     */
    public void deleteByTargetUser(String targetOpenId) {
        notificationMapper.deleteNotification(targetOpenId, null, null);
    }

    /**
     * 话题被删除了，删除由话题引起的通知信息
     *
     * @param topicId
     */
    public void deleteByTopic(Integer topicId) {
        notificationMapper.deleteNotification(null, null, topicId);
    }

}
