package com.hang.api;

import com.hang.annotation.OpenId;
import com.hang.service.NotificationService;
import com.hang.exceptions.ApiAssert;
import com.hang.pojo.vo.BaseRes;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanghang
 */
@Api("主题消息接口")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 根据消息查询方式返回不同结果
     * @param openId
     * @param pageNo
     * @param pageSize
     * @param type
     * @return
     */
    @ApiOperation("查看消息列表")
    @GetMapping("/getNotifications")
    public BaseRes getNotifications(@OpenId String openId,
                                    @RequestParam(required = false, defaultValue = "0") Integer pageNo,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize, Integer type) {
        ApiAssert.checkOpenId(openId);
        if (type.equals(1)) {
            return RespUtil.success(notificationService.findCommentNoteByOpenId(pageNo, pageSize, openId));
        } else {
            return RespUtil.success(notificationService.findSystemNoteByOpenId(pageNo, pageSize, openId));
        }
    }

    @ApiOperation("用户点击之后，将消息置于已读状态")
    @GetMapping("/updateByIsRead")
    public BaseRes updateByIsRead(@OpenId String openId) {
        ApiAssert.checkOpenId(openId);
        notificationService.updateByIsRead(openId);
        return RespUtil.success();
    }
}
