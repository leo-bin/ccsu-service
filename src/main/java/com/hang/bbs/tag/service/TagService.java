package com.hang.bbs.tag.service;


import com.hang.bbs.tag.mapper.TagMapper;
import com.hang.bbs.tag.pojo.Tag;
import com.hang.bbs.topic.mapper.TopicTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomoya at 2018/3/27
 * @author test
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TopicTagMapper topicTagMapper;

    public Tag findById(Integer id) {
        return tagMapper.selectByPrimaryKey(id);
    }

    public void save(Tag tag) {
        tagMapper.insertSelective(tag);
    }

    public void update(Tag tag) {
        tagMapper.updateByPrimaryKeySelective(tag);
    }

    public void save(List<Tag> tags) {
        for (Tag tag : tags) {
            this.save(tag);
        }
    }

    public Tag findByName(String name) {
        return tagMapper.findByName(name);
    }

    public List<Tag> save(String[] tags) {
        List<Tag> tagList = new ArrayList<>();
        for (String t : tags) {
            Tag tag = this.findByName(t);
            if (tag == null) {
                tag = new Tag();
                tag.setInTime(new Date());
                tag.setName(t);
                tag.setTopicCount(1);
                this.save(tag);
            } else {
                tag.setTopicCount(tag.getTopicCount() + 1);
                this.update(tag);
            }
            tagList.add(tag);
        }
        return tagList;
    }

    // 查询话题的标签
    public List<Tag> findByTopicId(Integer topicId) {
        return tagMapper.findByTopicId(topicId);
    }

    public void deleteById(Integer id) {
        tagMapper.deleteByPrimaryKey(id);
    }

    //同步标签的话题数
    public void async() {
        List<Tag> tags = tagMapper.findAll(null, null, null);
        //删除无效的关联
        topicTagMapper.deleteInValidAssociate();
        tags.forEach(tag -> {
            int count = topicTagMapper.countByTagId(tag.getId());
            tag.setTopicCount(count);
            this.update(tag);
        });
    }

    public List<Tag> findByNameLike(String name, Integer pageNo, Integer pageSize) {
        return tagMapper.findByNameLike("%" + name + "%", 0, 7);
    }
}
