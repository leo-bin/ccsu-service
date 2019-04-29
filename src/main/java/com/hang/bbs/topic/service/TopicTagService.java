package com.hang.bbs.topic.service;


import com.hang.bbs.tag.pojo.Tag;
import com.hang.bbs.tag.service.TagService;
import com.hang.bbs.topic.mapper.TopicTagMapper;
import com.hang.bbs.topic.pojo.TopicTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author test
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TopicTagService {

    @Autowired
    private TopicTagMapper topicTagMapper;

    @Autowired
    private TagService tagService;

    public void deleteByTopicId(Integer topicId) {
        // 先把标签的话题数都 - 1
        List<TopicTag> topicTags = topicTagMapper.findByTopicId(topicId);
        topicTags.forEach(topicTag -> {
            Tag tag = tagService.findById(topicTag.getTagId());
            tag.setTopicCount(tag.getTopicCount() - 1);
            tagService.update(tag);
        });
        // 操作完了，再删除中间表里的数据
        topicTagMapper.deleteByTopicId(topicId);
    }

    public List<TopicTag> save(List<Tag> tags, Integer topicId) {
        List<TopicTag> topicTags = new ArrayList<>();
        tags.forEach(_tag -> {
            TopicTag topicTag = new TopicTag();
            topicTag.setTagId(_tag.getId());
            topicTag.setTopicId(topicId);
            topicTagMapper.insertSelective(topicTag);
            topicTags.add(topicTag);
        });
        return topicTags;
    }

}
