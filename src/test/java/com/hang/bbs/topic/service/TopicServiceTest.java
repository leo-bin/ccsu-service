package com.hang.bbs.topic.service;

import com.hang.CcsuServiceApplicationTests;
import com.hang.bbs.common.Page;
import com.hang.bbs.tag.pojo.Tag;
import com.hang.bbs.tag.service.TagService;
import com.hang.bbs.topic.mapper.TopicMapper;
import com.hang.bbs.topic.pojo.TopicWithBLOBs;
import com.hang.manage.BbsCache;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TopicServiceTest extends CcsuServiceApplicationTests {

    @Autowired
    private TopicService topicService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TopicTagService topicTagService;

    @Autowired
    private BbsCache bbsCache;

    @Autowired
    private TopicMapper topicMapper;


    /**
     * 后台管理系统的测试
     */
    @Test
    public void page() {
        Page<Map> page = topicService.page(1, 10);
    }

    /**
     * 时间格式的测试
     * 我们用特定的时间格式来将Date()的时间转换成我们想要的时间格式
     * ("yyyy/MM/dd hh:mm:ss")：表示12小时制
     * ("yyyy/MM/dd HH:mm:ss")：表示24小时制
     */
    @Test
    public void dateTest(){
        System.out.println("Java自带日期方法的时间格式："+new Date());
        SimpleDateFormat sdf12 = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        SimpleDateFormat sdf24 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = sdf12.format(new Date());
        String date1 = sdf24.format(new Date());
        System.out.println("12小时制的时间格式："+date);
        System.out.println("24小时制的时间格式："+date1);
    }

    /**
     * 圈子创建话题测试
     */
    @Test
    public void createTopic(){
        String title="这是标题";
        String content="这是内容";
        String openId="xashuhsdakjsj5145341";
        SimpleDateFormat sdf24 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String intime=sdf24.format(new Date());
        TopicWithBLOBs topic = new TopicWithBLOBs();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setInTime(intime);
        topic.setView(0);
        topic.setOpenId(openId);
        topic.setCommentCount(0);
        topic.setGood(false);
        topic.setTop(false);
        topic.setUp(0);
        topic.setDown(0);
        topic.setUpIds("");
        topic.setDownIds("");
        topic.setTag("标签");
        topic.setWeight(0.0);
        topic = this.save(topic);
    }

    public TopicWithBLOBs save(TopicWithBLOBs topic) {

        bbsCache.saveTopicById(topic.getId(), topic);
        topicMapper.insertSelective(topic);
        return topic;
    }


}