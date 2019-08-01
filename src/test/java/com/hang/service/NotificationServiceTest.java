package com.hang.service;

import com.hang.CcsuServiceApplicationTests;
import com.hang.bbs.common.Page;
import com.hang.dao.NotificationDAO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


public class NotificationServiceTest extends CcsuServiceApplicationTests {

    @Autowired
    private NotificationDAO notificationDAO;

    @Test
    public void findCommentNoteByOpenId() {
        Integer pageNo=1;
        Integer pageSize=10;
        String targetOpenId="nishdhiudhsh";
        List<Map> list = notificationDAO.findSystemNoteByOpenId(targetOpenId, (pageNo - 1) * pageSize, pageSize,
                "n.is_read asc, n.id desc");
        int count = notificationDAO.countByTargetOpenId(targetOpenId);
        Page<Map> page=new Page<>(pageNo, pageSize, count, list);
        System.out.println(page);
    }

    @Test
    public void findSystemNoteByOpenId() {
    }
}