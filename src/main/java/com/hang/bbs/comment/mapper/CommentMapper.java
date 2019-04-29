package com.hang.bbs.comment.mapper;

import com.hang.bbs.comment.pojo.Comment;
import com.hang.bbs.comment.pojo.CommentWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommentWithBLOBs record);

    int insertSelective(CommentWithBLOBs record);

    CommentWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommentWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CommentWithBLOBs record);

    int updateByPrimaryKey(Comment record);

    List<Map> findByTopicId(Integer topicId);

    int countByTopicId(Integer topicId);

    void deleteByTopicId(Integer topicId);

    void deleteByUserId(Integer userId);

    List<Map> findAllForAdmin(
            @Param("pageNo") Integer pageNo,
            @Param("pageSize") Integer pageSize,
            @Param("orderBy") String orderBy
    );

    int countAllForAdmin();

    List<CommentWithBLOBs> findCommentByTopicId(Integer topicId);

    List<Map> findByUserId(
            @Param("openId") Integer userId,
            @Param("pageNo") Integer pageNo,
            @Param("pageSize") Integer pageSize,
            @Param("orderBy") String orderBy
    );

    int countByUserId(Integer userId);

    List<CommentWithBLOBs> findChildByCommentId(@Param("commentId") Integer commentId);

}