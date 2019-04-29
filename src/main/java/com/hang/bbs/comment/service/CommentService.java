package com.hang.bbs.comment.service;

import com.hang.bbs.common.Page;
import com.hang.bbs.comment.mapper.CommentMapper;
import com.hang.bbs.comment.pojo.CommentWithBLOBs;
import com.hang.bbs.topic.pojo.TopicWithBLOBs;
import com.hang.bbs.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author test
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TopicService topicService;

    public CommentWithBLOBs findById(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    public void save(CommentWithBLOBs comment) {
        commentMapper.insertSelective(comment);
    }

    public void update(CommentWithBLOBs commentWithBLOBs) {
        commentMapper.updateByPrimaryKeySelective(commentWithBLOBs);
    }

    public CommentWithBLOBs update(TopicWithBLOBs topic, CommentWithBLOBs oldComment, CommentWithBLOBs comment, Integer userId) {
        this.update(comment);
        return comment;
    }

    public void delete(Integer id, Integer userId) {
        CommentWithBLOBs comment = this.findById(id);
        if (comment != null) {
            TopicWithBLOBs topic = topicService.findById(comment.getTopicId());
            topic.setCommentCount(topic.getCommentCount() - 1);
            //删除子评论
            List<CommentWithBLOBs> commentWithBLOBs = commentMapper.findChildByCommentId(id);
            if (commentWithBLOBs != null && commentWithBLOBs.size() > 0) {
                for (CommentWithBLOBs commentWithBLOB : commentWithBLOBs) {
                    // 日志
                    topic.setCommentCount(topic.getCommentCount() - 1);
                    commentMapper.deleteByPrimaryKey(commentWithBLOB.getId());
                }
            }
            topicService.update(topic);
            commentMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 删除用户发布的所有评论
     *
     * @param userId
     */
    public void deleteByUserId(Integer userId) {
        commentMapper.deleteByUserId(userId);
    }

    /**
     * 根据话题删除评论
     *
     * @param topicId
     */
    public void deleteByTopic(Integer topicId) {
        commentMapper.deleteByTopicId(topicId);
    }

    public CommentWithBLOBs createComment(String openId, TopicWithBLOBs topic, Integer commentId, String content) {
        CommentWithBLOBs comment = new CommentWithBLOBs();
        comment.setCommentId(commentId);
        comment.setOpenId(openId);
        comment.setTopicId(topic.getId());
        comment.setInTime(new Date());
        comment.setUp(0);
        comment.setDown(0);
        comment.setUpIds("");
        comment.setDownIds("");
        comment.setContent(content);
        this.save(comment);

        //评论+1
        topic.setCommentCount(topic.getCommentCount() + 1);
        topic.setLastCommentTime(new Date());
        topicService.update(topic);
        return comment;
    }

    /**
     * 根据话题查询评论列表
     *
     * @param topicId
     * @return
     */
    public List<Map> findCommentWithTopic(Integer topicId) {
        List<Map> comments = commentMapper.findByTopicId(topicId);
        // 初始深度为1
        return sortLayer(comments, new ArrayList<>(), 1);
    }

    public List<CommentWithBLOBs> findByTopicId(Integer topicId) {
        return commentMapper.findCommentByTopicId(topicId);
    }

    private List<Map> sortLayer(List<Map> comments, List<Map> newComments, Integer layer) {
        if (comments == null || comments.size() == 0) {
            return newComments;
        }
        if (newComments.size() == 0) {
            comments.forEach(map -> {
                if (map.get("comment_id") == null) {
                    map.put("layer", layer);
                    newComments.add(map);
                }
            });
            comments.removeAll(newComments);
            return sortLayer(comments, newComments, layer + 1);
        } else {
            for (int index = 0; index < newComments.size(); index++) {
                Map newComment = newComments.get(index);

                List<Map> findComments = new ArrayList<>();
                comments.forEach(map -> {
                    if (Objects.equals(map.get("comment_id"), newComment.get("id"))) {
                        map.put("layer", layer);
                        findComments.add(map);
                    }
                });
                comments.removeAll(findComments);

                newComments.addAll(newComments.indexOf(newComment) + 1, findComments);
                index = newComments.indexOf(newComment) + findComments.size();
            }
            return sortLayer(comments, newComments, layer + 1);
        }
    }

    /**
     * 查询用户的评论列表
     *
     * @return
     */
    public Page<Map> findByUser(Integer pageNo, Integer pageSize, Integer userId) {
        List<Map> list = commentMapper.findByUserId(userId, (pageNo - 1) * pageSize, pageSize, "c.id desc");
        int count = commentMapper.countByUserId(userId);
        return new Page<>(pageNo, pageSize, count, list);
    }

    // 后台评论列表
    public Page<Map> findAllForAdmin(Integer pageNo, Integer pageSize) {
        List<Map> list = commentMapper.findAllForAdmin((pageNo - 1) * pageSize, pageSize, "c.id desc");
        int count = commentMapper.countAllForAdmin();
        return new Page<>(pageNo, pageSize, count, list);
    }
}
