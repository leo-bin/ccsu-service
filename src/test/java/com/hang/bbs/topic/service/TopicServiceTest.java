package com.hang.bbs.topic.service;

import com.hang.CcsuServiceApplicationTests;
import com.hang.bbs.common.Page;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.*;

public class TopicServiceTest extends CcsuServiceApplicationTests {

    @Autowired
    private TopicService topicService;

    @Test
    public void page() {
        Page<Map> page = topicService.page(1, 10);
    }
}