package com.hang.manage;

import com.hang.bbs.tag.pojo.Tag;
import com.hang.bbs.topic.pojo.Topic;
import com.hang.bbs.topic.pojo.TopicWithBLOBs;
import com.hang.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hang.constant.CacheConstant.BBS_CACHE_TAG_PREFIX;
import static com.hang.constant.CacheConstant.BBS_CACHE_TOPIC_PREFIX;

/**
 * @author zhanghang
 */
@Service
public class BbsCache {

    @Autowired
    private RedisUtil redisUtil;

    public void saveTagById(Integer id, Tag tag) {
        redisUtil.set(BBS_CACHE_TAG_PREFIX + id, tag);
    }

    public Tag getTagById(Integer id) {
        return (Tag) redisUtil.get(BBS_CACHE_TAG_PREFIX + id);
    }

    public void removeTagById(Integer id) {
        redisUtil.del(BBS_CACHE_TAG_PREFIX + id);
    }

    public void saveTagByName(String name, Tag tag) {
        redisUtil.set(BBS_CACHE_TAG_PREFIX + name, tag);
    }

    public Tag getTagByName(String name) {
        return (Tag) redisUtil.get(BBS_CACHE_TAG_PREFIX + name);
    }

    public void saveTopicById(Integer id, TopicWithBLOBs topic) {
        redisUtil.set(BBS_CACHE_TOPIC_PREFIX + id, topic);
    }

    public TopicWithBLOBs getTopicById(Integer id) {
        return (TopicWithBLOBs) redisUtil.get(BBS_CACHE_TOPIC_PREFIX + id);
    }

    public void removeTopicById(Integer id) {
        redisUtil.del(BBS_CACHE_TOPIC_PREFIX + id);
    }

}
