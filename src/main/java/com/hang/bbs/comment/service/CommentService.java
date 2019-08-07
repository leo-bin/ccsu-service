package com.hang.bbs.comment.service;

import com.google.common.collect.Lists;
import com.hang.bbs.comment.pojo.Comment;
import com.hang.bbs.common.Page;
import com.hang.bbs.comment.mapper.CommentMapper;
import com.hang.bbs.comment.pojo.CommentWithBLOBs;
import com.hang.bbs.common.VoteAction;
import com.hang.enums.NotificationEnum;
import com.hang.service.NotificationService;
import com.hang.bbs.topic.pojo.TopicWithBLOBs;
import com.hang.bbs.topic.service.TopicService;
import com.hang.pojo.data.UserInfoDO;
import com.hang.service.UserService;
import io.swagger.models.auth.In;
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
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    public CommentWithBLOBs findById(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存评论
     * @param comment
     * @return
     */
    public Integer save(CommentWithBLOBs comment) {
        int i=commentMapper.insertSelective(comment);
        //获取评论Id
        int commentId=comment.getId();
        if (i>=0&&commentId>=0){
            return commentId;
        }
        return null;
    }

    public void update(CommentWithBLOBs commentWithBLOBs) {
        commentMapper.updateByPrimaryKeySelective(commentWithBLOBs);
    }

    public CommentWithBLOBs update(TopicWithBLOBs topic, CommentWithBLOBs oldComment, CommentWithBLOBs comment, String openId) {
        this.update(comment);
        return comment;
    }

    public void delete(Integer id, String openId) {
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
     * @param openId
     */
    public void deleteByOpenId(String openId) {
        commentMapper.deleteByOpenId(openId);
    }

    /**
     * 根据话题删除评论
     *
     * @param topicId
     */
    public void deleteByTopic(Integer topicId) {
        commentMapper.deleteByTopicId(topicId);
    }

    /**
     * 创建评论
     *
     * @param openId
     * @param topic
     * @param content
     * @return
     */
    public CommentWithBLOBs createComment(String openId, TopicWithBLOBs topic,String content) {
        CommentWithBLOBs comment = new CommentWithBLOBs();
        Integer commentId;
        comment.setOpenId(openId);
        comment.setTopicId(topic.getId());
        comment.setInTime(new Date());
        comment.setUp(0);
        comment.setDown(0);
        comment.setUpIds("");
        comment.setDownIds("");
        comment.setContent(content);
        commentId=this.save(comment);

        //评论+1
        topic.setCommentCount(topic.getCommentCount() + 1);
        topic.setLastCommentTime(new Date());
        topicService.update(topic);

        //回复
        if (commentId != null) {
            Comment replyComment = this.findById(commentId);
            if (!openId.equals(replyComment.getOpenId())) {
                notificationService.sendNotification(openId, replyComment.getOpenId(), NotificationEnum.REPLY, topic.getId(), content,"");
            }
        }

        //不能自己给自己发通知
         if (!topic.getOpenId().equals(openId)) {
             notificationService.sendNotification(openId, topic.getOpenId(), NotificationEnum.COMMENT, topic.getId(), content,"");
         }
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

    /**
     * 递归排序
     *
     * @param comments
     * @param newComments
     * @param layer
     * @return
     */
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
    public Page<Map> findByUser(Integer pageNo, Integer pageSize, String openId) {
        List<Map> list = commentMapper.findByOpenId(openId, (pageNo - 1) * pageSize, pageSize, "c.id desc");
        int count = commentMapper.countByOpenId(openId);
        return new Page<>(pageNo, pageSize, count, list);
    }

    /**
     * 后台评论列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<Map> findAllForAdmin(Integer pageNo, Integer pageSize) {
        List<Map> list = commentMapper.findAllForAdmin((pageNo - 1) * pageSize, pageSize, "c.id desc");
        int count = commentMapper.countAllForAdmin();
        return new Page<>(pageNo, pageSize, count, list);
    }

    /**
     * 对评论投票
     *
     * @param openId
     * @param comment
     */
    public Map<String, Object> vote(String openId, CommentWithBLOBs comment, String action) {
        Map<String, Object> map = new HashMap<>(16);
        List<String> upIds = new ArrayList<>();
        List<String> downIds = new ArrayList<>();
        UserInfoDO commentUser = userService.getUserInfoByOpenId(comment.getOpenId());
        if (!StringUtils.isEmpty(comment.getUpIds())) {
            upIds = Lists.newArrayList(comment.getUpIds().split(","));
        }
        if (!StringUtils.isEmpty(comment.getDownIds())) {
            downIds = Lists.newArrayList(comment.getDownIds().split(","));
        }

        if (action.equals(VoteAction.UP.name())) {
            // 如果点踩ID里有，就删除，并将down - 1
            if (downIds.contains(openId)) {
                comment.setDown(comment.getDown() - 1);
                downIds.remove(openId);
            }
            // 如果点赞ID里没有，就添加上，并将up + 1
            if (!upIds.contains(openId)) {
                upIds.add(openId);
                comment.setUp(comment.getUp() + 1);
                map.put("isUp", true);
                map.put("isDown", false);
            } else {
                upIds.remove(openId);
                comment.setUp(comment.getUp() - 1);
                map.put("isUp", false);
                map.put("isDown", false);
            }
        } else if (action.equals(VoteAction.DOWN.name())) {
            // 如果点赞ID里有，就删除，并将up - 1
            if (upIds.contains(openId)) {
                comment.setUp(comment.getUp() - 1);
                upIds.remove(openId);
            }
            // 如果点踩ID里没有，就添加上，并将down + 1
            if (!downIds.contains(openId)) {
                downIds.add(openId);
                comment.setDown(comment.getDown() + 1);
                map.put("isUp", false);
                map.put("isDown", true);
            } else {
                downIds.remove(openId);
                comment.setDown(comment.getDown() - 1);
                map.put("isUp", false);
                map.put("isDown", false);
            }
        }
        map.put("commentId", comment.getId());
        map.put("up", comment.getUp());
        map.put("down", comment.getDown());
        map.put("vote", comment.getUp() - comment.getDown());
        comment.setUpIds(StringUtils.collectionToCommaDelimitedString(upIds));
        comment.setDownIds(StringUtils.collectionToCommaDelimitedString(downIds));
        update(comment);

        // 通知
        notificationService.sendNotification(openId, commentUser.getOpenId(), NotificationEnum.UP, comment.getTopicId(), null,"");
        return map;
    }
}
