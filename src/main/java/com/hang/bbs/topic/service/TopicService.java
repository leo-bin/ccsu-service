package com.hang.bbs.topic.service;

import com.hang.bbs.common.Page;
import com.hang.bbs.comment.service.CommentService;
import com.hang.bbs.tag.pojo.Tag;
import com.hang.bbs.tag.service.TagService;
import com.hang.bbs.topic.mapper.TopicMapper;
import com.hang.bbs.topic.pojo.Topic;
import com.hang.bbs.topic.pojo.TopicWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author test
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TopicTagService topicTagService;

    @Autowired
    private TagService tagService;

    public TopicWithBLOBs createTopic(String title, String content, String tag, String openId) {
        TopicWithBLOBs topic = new TopicWithBLOBs();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setInTime(new Date());
        topic.setView(0);
        topic.setOpenId(openId);
        topic.setCommentCount(0);
        topic.setGood(false);
        topic.setTop(false);
        topic.setUp(0);
        topic.setDown(0);
        topic.setUpIds("");
        topic.setDownIds("");
        topic.setTag(tag);
        topic.setWeight(0.0);
        topic = this.save(topic);
        // 处理标签
        List<Tag> tagList = tagService.save(tag.split(","));
        topicTagService.save(tagList, topic.getId());
        return topic;
    }

    public TopicWithBLOBs updateTopic(Topic oldTopic, TopicWithBLOBs topic, String openId) {
        this.update(topic);
        // 处理标签
        topicTagService.deleteByTopicId(topic.getId());
        List<Tag> tagList = tagService.save(topic.getTag().split(","));
        topicTagService.save(tagList, topic.getId());
        return topic;
    }

    public TopicWithBLOBs save(TopicWithBLOBs topic) {
        topicMapper.insertSelective(topic);
        return topic;
    }

    public void update(TopicWithBLOBs topic) {
        topicMapper.updateByPrimaryKeySelective(topic);
    }

    public TopicWithBLOBs findById(Integer id) {
        return topicMapper.selectByPrimaryKey(id);
    }

    public void deleteById(Integer id, String openId) {
        Topic topic = findById(id);
        if (topic != null) {
            /// 删除话题下面的评论
            /// commentService.deleteByTopic(id);
            //删除话题
            topicMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 删除用户的所有话题，这里不做日志记录了，
     * 这方法只会在后台被管理员删除用户时调用，
     * 同时也会删除这个用户的所有日志，所以不用做日志记录
     *
     * @param openId
     */
    public void deleteByopenId(String openId) {
        topicMapper.deleteByOpenId(openId);
    }

    public Page<Map> page(Integer pageNo, Integer pageSize, String tab) {
        if (tab.equalsIgnoreCase("good")) {
            List<Map> list = topicMapper.findTopic(null, true, null, null, null, null, (pageNo - 1) * pageSize, pageSize, "t.top desc, t.weight desc, t.id desc");
            int count = topicMapper.countTopic(null, true, null, null, null, null);
            return new Page<>(pageNo, pageSize, count, list);
        } else if (tab.equalsIgnoreCase("newest")) {
            List<Map> list = topicMapper.findTopic(null, null, null, null, null, null, (pageNo - 1) * pageSize, pageSize, "t.id desc, t.weight desc");
            int count = topicMapper.countTopic(null, null, null, null, null, null);
            return new Page<>(pageNo, pageSize, count, list);
        } else if (tab.equalsIgnoreCase("noanswer")) {
            List<Map> list = topicMapper.findTopic(null, null, null, 0, null, null, (pageNo - 1) * pageSize, pageSize, "t.top desc, t.weight desc, t.id desc");
            int count = topicMapper.countTopic(null, null, null, 0, null, null);
            return new Page<>(pageNo, pageSize, count, list);
        } else {
            List<Map> list = topicMapper.findTopic(null, null, null, null, null, null, (pageNo - 1) * pageSize, pageSize, "t.top desc, t.weight desc, t.id desc");
            int count = topicMapper.countTopic(null, null, null, null, null, null);
            return new Page<>(pageNo, pageSize, count, list);
        }
    }

    public Page<Map> pageByTagId(Integer pageNo, Integer pageSize, Integer tagId) {
        List<Map> list = topicMapper.findTopicsByTagId(tagId, (pageNo - 1) * pageSize, pageSize, "t.weight desc, t.id desc");
        int count = topicMapper.countTopicsByTagId(tagId);
        return new Page<>(pageNo, pageSize, count, list);
    }

    /**
     * 查询用户的话题
     *
     * @return
     */
    public Page<Map> findByOpenId(Integer pageNo, Integer pageSize, String openId) {
        List<Map> list = topicMapper.findTopic(openId, null, null, null, null, null, (pageNo - 1) * pageSize, pageSize, "t.id desc");
        int count = topicMapper.countTopic(openId, null, null, null, null, null);
        return new Page<>(pageNo, pageSize, count, list);
    }

    /**
     * 根据标题查询话题（防止发布重复话题）
     *
     * @param title
     * @return
     */
    public Topic findByTitle(String title) {
        return topicMapper.findByTitle(title);
    }

    public Page<Map> findAllForAdmin(Integer pageNo, Integer pageSize, String openId, String startTime, String endTime,
                                     String status) {
        Boolean good = null, top = null;
        if (!StringUtils.isEmpty(status)) {
            if (status.equals("good")) {
                good = true;
            } else if (status.equals("top")) {
                top = true;
            }
        }
        if ("".equals(startTime)) startTime = null;
        if ("".equals(endTime)) endTime = null;
        List<Map> list = topicMapper.findTopic(openId, good, top, null, startTime, endTime, (pageNo - 1) * pageSize, pageSize, "t.top desc, t.weight desc, t.id desc");
        int count = topicMapper.countTopic(openId, good, top, null, startTime, endTime);
        return new Page<>(pageNo, pageSize, count, list);
    }

}
