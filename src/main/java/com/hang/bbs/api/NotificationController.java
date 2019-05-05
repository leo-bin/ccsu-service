package com.hang.bbs.api;

import com.hang.annotation.OpenId;
import com.hang.bbs.notification.service.NotificationService;
import com.hang.exceptions.ApiAssert;
import com.hang.pojo.vo.BaseRes;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanghang
 */
@Api("主题评论消息接口")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @ApiOperation("查看消息列表")
    @GetMapping("/getNotifications")
    public BaseRes getNotifications(@OpenId String openId, String pageNo, String pageSize) {
        ApiAssert.checkOpenId(openId);
        return RespUtil.success(notificationService.findByTargetUserAndIsRead(1, 10, openId));
    }

    @ApiOperation("用户点击之后，讲消息置于已读状态")
    @GetMapping("/updateByIsRead")
    public BaseRes updateByIsRead(@OpenId String openId) {
        ApiAssert.checkOpenId(openId);
        notificationService.updateByIsRead(openId);
        return RespUtil.success();
    }

}
