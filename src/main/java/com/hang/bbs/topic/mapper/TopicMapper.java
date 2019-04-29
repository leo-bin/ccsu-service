package com.hang.bbs.topic.mapper;

import com.hang.bbs.topic.pojo.Topic;
import com.hang.bbs.topic.pojo.TopicWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author test
 */
@Repository
public interface TopicMapper {
  int deleteByPrimaryKey(Integer id);

  int insert(TopicWithBLOBs record);

  int insertSelective(TopicWithBLOBs record);

  TopicWithBLOBs selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(TopicWithBLOBs record);

  int updateByPrimaryKeyWithBLOBs(TopicWithBLOBs record);

  int updateByPrimaryKey(Topic record);

  List<Map> findTopic(
          @Param("openId") String openId,
          @Param("good") Boolean good,
          @Param("top") Boolean top,
          @Param("commentCount") Integer commentCount,
          @Param("startTime") String startTime,
          @Param("endTime") String endTime,
          @Param("pageNo") Integer pageNo,
          @Param("pageSize") Integer pageSize,
          @Param("orderBy") String orderBy
  );

  int countTopic(
          @Param("openId") String openId,
          @Param("good") Boolean good,
          @Param("top") Boolean top,
          @Param("commentCount") Integer commentCount,
          @Param("startTime") String startTime,
          @Param("endTime") String endTime
  );

  void deleteByOpenId(String openId);

  Topic findByTitle(String title);

  List<Map> findTopicsByTagId(
          @Param("tagId") Integer tagId,
          @Param("pageNo") Integer pageNo,
          @Param("pageSize") Integer pageSize,
          @Param("orderBy") String orderBy
  );

  int countTopicsByTagId(Integer tagId);

  List<TopicWithBLOBs> findAll();
}