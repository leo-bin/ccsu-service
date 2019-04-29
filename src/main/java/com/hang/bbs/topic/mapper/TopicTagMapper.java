package com.hang.bbs.topic.mapper;


import com.hang.bbs.topic.pojo.TopicTag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicTagMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TopicTag record);

    int insertSelective(TopicTag record);

    TopicTag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TopicTag record);

    int updateByPrimaryKey(TopicTag record);

    //自定义方法
    List<TopicTag> findByTopicId(Integer topicId);

    int countByTagId(Integer tagId);

    void deleteByTopicId(Integer topicId);

    void deleteInValidAssociate();
}