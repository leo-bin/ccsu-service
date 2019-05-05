package com.hang.bbs.tag.service;


import com.hang.bbs.tag.mapper.TagMapper;
import com.hang.bbs.tag.pojo.Tag;
import com.hang.bbs.topic.mapper.TopicTagMapper;
import com.hang.manage.BbsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by tomoya at 2018/3/27
 *
 * @author test
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TopicTagMapper topicTagMapper;

    @Autowired
    private BbsCache bbsCache;

    public Tag findById(Integer id) {
        Tag tag = bbsCache.getTagById(id);
        if (Objects.isNull(tag)) {
            tag = tagMapper.selectByPrimaryKey(id);
            bbsCache.saveTagById(id, tag);
        }
        return tag;
    }

    public void save(Tag tag) {
        tagMapper.insertSelective(tag);
    }

    public void update(Tag tag) {
        tagMapper.updateByPrimaryKeySelective(tag);
        bbsCache.saveTagById(tag.getId(), tag);
    }

    public void save(List<Tag> tags) {
        for (Tag tag : tags) {
            this.save(tag);
        }
    }

    public Tag findByName(String name) {
        Tag tag = bbsCache.getTagByName(name);
        if (Objects.isNull(tag)) {
            tag = tagMapper.findByName(name);
            bbsCache.saveTagByName(name, tag);
        }
        return tag;
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

    /**
     * 查询话题的标签
     *
     * @param topicId
     * @return
     */
    public List<Tag> findByTopicId(Integer topicId) {
        return tagMapper.findByTopicId(topicId);
    }

    public void deleteById(Integer id) {
        bbsCache.removeTagById(id);
        tagMapper.deleteByPrimaryKey(id);
    }

    /**
     * 同步标签的话题数
     */
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
