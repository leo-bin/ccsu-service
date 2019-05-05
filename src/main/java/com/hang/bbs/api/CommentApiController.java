package com.hang.bbs.api;


import com.hang.annotation.OpenId;
import com.hang.bbs.comment.pojo.Comment;
import com.hang.bbs.comment.pojo.CommentWithBLOBs;
import com.hang.bbs.comment.service.CommentService;
import com.hang.bbs.common.VoteAction;
import com.hang.bbs.topic.pojo.TopicWithBLOBs;
import com.hang.bbs.topic.service.TopicService;
import com.hang.exceptions.ApiAssert;
import com.hang.pojo.vo.BaseRes;
import com.hang.utils.EnumUtil;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author test
 */
@Api("主题评论接口")
@RestController
@RequestMapping("/api/comment")
public class CommentApiController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private CommentService commentService;

    /**
     * 话题的评论列表
     *
     * @param topicId 话题ID
     * @return
     */
    @ApiOperation("主题的评论列表")
    @GetMapping("/list")
    public BaseRes list(@RequestParam Integer topicId) {
        return RespUtil.success(commentService.findCommentWithTopic(topicId));
    }

    /**
     * 保存评论
     * 回复的评论ID，可以为null
     *
     * @param topicId 话题ID
     * @param content 评论内容
     * @return
     */
    @ApiOperation("对某个主题进行评价")
    @PostMapping("/save")
    public BaseRes save(@OpenId String openId, Integer topicId, String content) {
        ApiAssert.notEmpty(content, "评论内容不能为空");
        ApiAssert.notNull(topicId, "话题ID不存在");
        ApiAssert.checkOpenId(openId);

        TopicWithBLOBs topic = topicService.findById(topicId);
        ApiAssert.notNull(topic, "回复的话题不存在");

        Comment comment = commentService.createComment(openId, topic, null, content);
        return RespUtil.success(comment);
    }

    /**
     * 对评论进行编辑
     *
     * @param id      评论ID
     * @param content 评论内容
     * @return
     */
    @ApiOperation("对某个评论进行编辑")
    @PostMapping("/edit")
    public BaseRes edit(@OpenId String openId, Integer id, String content) {
        ApiAssert.checkOpenId(openId);
        ApiAssert.notEmpty(content, "评论内容不能为空");
        CommentWithBLOBs comment = commentService.findById(id);
        CommentWithBLOBs oldComment = comment;
        comment.setContent(content);
        commentService.update(comment);
        TopicWithBLOBs topic = topicService.findById(comment.getTopicId());
        comment = commentService.update(topic, oldComment, comment, openId);
        return RespUtil.success(comment);
    }

    /**
     * 对评论投票
     *
     * @param id     评论ID
     * @param action 评论动作，只能填 UP, DOWN
     * @return
     */
    @ApiOperation("对评论投票")
    @GetMapping("/{id}/vote")
    public BaseRes vote(@OpenId String openId, @PathVariable Integer id, String action) {
        ApiAssert.checkOpenId(openId);
        CommentWithBLOBs comment = commentService.findById(id);

        ApiAssert.notNull(comment, "评论不存在");
        ApiAssert.notTrue(openId.equals(comment.getOpenId()), "不能给自己的评论投票");
        ApiAssert.isTrue(EnumUtil.isDefined(VoteAction.values(), action), "参数错误");

        Map<String, Object> map = commentService.vote(openId, comment, action);
        return RespUtil.success(map);
    }

}
