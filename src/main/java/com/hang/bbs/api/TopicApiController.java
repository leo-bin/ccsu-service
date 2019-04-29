package com.hang.bbs.api;

import com.hang.annotation.OpenId;
import com.hang.bbs.common.VoteAction;
import com.hang.bbs.tag.pojo.Tag;
import com.hang.bbs.tag.service.TagService;
import com.hang.bbs.topic.pojo.Topic;
import com.hang.bbs.topic.pojo.TopicWithBLOBs;
import com.hang.bbs.topic.service.TopicService;
import com.hang.constant.WxConstant;
import com.hang.exceptions.ApiAssert;
import com.hang.pojo.vo.BaseRes;
import com.hang.utils.EnumUtil;
import com.hang.utils.RespUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author test
 */
@RestController
@RequestMapping("/api/topic")
public class TopicApiController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private TagService tagService;

    @GetMapping("/{id}")
    public BaseRes detail(@PathVariable Integer id) {
        Map<String, Object> map = new HashMap<>(16);
        TopicWithBLOBs topic = topicService.findById(id);

        // 浏览量+1
        topic.setView(topic.getView() + 1);
        // 更新话题数据
        topicService.update(topic);
        map.put("topic", topic);
        map.put("collect", null);
        // 查询这个话题被收藏的个数
        List<Tag> tags = tagService.findByTopicId(id);
        map.put("tags", tags);
        return RespUtil.success(map);
    }

    /**
     * 保存话题
     *
     * @param title   话题标题
     * @param content 话题内容
     * @param tag     话题标签，格式是 , 隔开的字符串（英文下的逗号）
     * @return
     */
    @GetMapping("/save")
    public BaseRes save(@OpenId String openId, String title, String content, String tag) {
        ApiAssert.notEmpty(title, "请输入标题");
        ApiAssert.notEmpty(tag, "标签不能为空");

        /// ApiAssert.notTrue(topicService.findByTitle(title) != null, "话题标题已经存在");
        Topic topic = topicService.createTopic(title, content, tag, openId);
        return RespUtil.success(topic);
    }

    /**
     * 话题编辑
     *
     * @param id      话题ID
     * @param title   话题标题
     * @param content 话题内容
     * @param tag     话题标签，格式是 , 隔开的字符串（英文下的逗号）
     * @return
     */
    @GetMapping("/edit")
    public BaseRes update(@OpenId String openId, Integer id, String title, String content, String tag) {
        ApiAssert.notEmpty(title, "请输入标题");
        ApiAssert.notEmpty(content, "请输入内容");
        ApiAssert.notEmpty(tag, "标签不能为空");

        TopicWithBLOBs oldTopic = topicService.findById(id);
        ApiAssert.isTrue(oldTopic.getOpenId().equals(openId), "不能修改别人的话题");

        TopicWithBLOBs topic = oldTopic;
        topic.setTitle(title);
        topic.setContent(content);
        topic.setTag(tag);
        topic = topicService.updateTopic(oldTopic, topic, openId);
        return RespUtil.success(topic);
    }

    /**
     * 给话题投票
     *
     * @param id     话题ID
     * @param action 赞成或者反对，只能填：UP, DOWN
     * @return
     */
    @GetMapping("/{id}/vote")
    public BaseRes vote(@OpenId String openId, @PathVariable Integer id, String action) {
        TopicWithBLOBs topic = topicService.findById(id);
        ApiAssert.notNull(topic, "话题不存在");
        ApiAssert.notTrue(openId.equals(topic.getOpenId()), "不能给自己的话题投票");
        ApiAssert.isTrue(EnumUtil.isDefined(VoteAction.values(), action), "参数错误");

        Map<String, Object> map = topicService.vote(openId, topic, action);
        return RespUtil.success(map);
    }
}
