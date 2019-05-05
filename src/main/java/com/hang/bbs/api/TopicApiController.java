package com.hang.bbs.api;

import com.hang.annotation.OpenId;
import com.hang.aop.StatisticsTime;
import com.hang.bbs.common.Page;
import com.hang.bbs.common.VoteAction;
import com.hang.bbs.tag.pojo.Tag;
import com.hang.bbs.tag.service.TagService;
import com.hang.bbs.topic.pojo.Topic;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author test
 */
@Api("主题接口")
@RestController
@RequestMapping("/api/topic")
public class TopicApiController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private TagService tagService;

    /**
     * 首页接口
     *
     * @param pageNo 页数
     * @return Page对象，里面有分页信息
     */
    @StatisticsTime("index")
    @ApiOperation("请求主题列表页")
    @GetMapping("/index")
    public BaseRes index(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "5") Integer pageSize) {
        Page<Map> page = topicService.page(pageNo, pageSize);
        return RespUtil.success(page);
    }

    @StatisticsTime("remove")
    @ApiOperation("删除主题")
    @GetMapping("/remove/{id}")
    public BaseRes remove(@OpenId String openId, @PathVariable Integer id) {
        ApiAssert.checkOpenId(openId);
        topicService.deleteById(id, openId);
        return RespUtil.success();
    }

    @StatisticsTime("detail")
    @ApiOperation("查看主题详情")
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
    @StatisticsTime("save")
    @ApiOperation("新建主题")
    @GetMapping("/save")
    public BaseRes save(@OpenId String openId, String title, String content, String tag) {
        ApiAssert.checkOpenId(openId);
        ApiAssert.notEmpty(title, "请输入标题");
        ApiAssert.notEmpty(tag, "标签不能为空");
        ApiAssert.notEmpty(content, "内容不能为空");

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
    @StatisticsTime("edit")
    @ApiOperation("编辑主题")
    @GetMapping("/edit")
    public BaseRes update(@OpenId String openId, Integer id, String title, String content, String tag) {
        ApiAssert.checkOpenId(openId);
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
     * @param id 话题ID
     * @return
     */
    @StatisticsTime("vote")
    @ApiOperation("为主题点赞")
    @GetMapping("/{id}/vote")
    public BaseRes vote(@OpenId String openId, @PathVariable Integer id) {
        ApiAssert.checkOpenId(openId);
        TopicWithBLOBs topic = topicService.findById(id);
        ApiAssert.notNull(topic, "话题不存在");
        ApiAssert.notTrue(openId.equals(topic.getOpenId()), "不能给自己的话题投票");

        Map<String, Object> map = topicService.vote(openId, topic, VoteAction.UP.name());
        return RespUtil.success(map);
    }
}
