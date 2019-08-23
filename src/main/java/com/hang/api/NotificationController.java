package com.hang.api;

import com.hang.annotation.OpenId;
import com.hang.dao.NotificationDAO;
import com.hang.dao.TeamDAO;
import com.hang.enums.NotificationEnum;
import com.hang.pojo.data.StudentDO;
import com.hang.pojo.data.SystemNotificationDO;
import com.hang.pojo.data.TeamDO;
import com.hang.pojo.vo.TeamVO;
import com.hang.service.NotificationService;
import com.hang.exceptions.ApiAssert;
import com.hang.pojo.vo.BaseRes;
import com.hang.service.StudentService;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.hang.constant.InformationConstant.INVITATION_SUCCESS_SUFFIX;
import static com.hang.constant.InformationConstant.SYSTEM_NOTIFICATION_SUFFIX;

/**
 * @author zhanghang
 */
@Api("主题消息接口")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private TeamDAO teamDAO;

    /**
     * 根据消息查询方式返回不同结果
     */
    @ApiOperation("查看消息列表")
    @GetMapping("/getNotifications")
    public BaseRes getNotifications(@OpenId String openId,
                                    @RequestParam(required = false, defaultValue = "1") Integer pageNo,
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


    /**
     * 团队邀请成员时发通知
     */
    @ApiOperation("发系统通知")
    @PostMapping("/sendNotification")
    public BaseRes sendNotification(@OpenId String openId,
                                    @RequestParam String jwcAccount,
                                    @RequestParam Integer teamId){
        ApiAssert.checkOpenId(openId);
        StudentDO studentDO=studentService.getStudentInfoByJwcAccount(jwcAccount);
        TeamDO teamDO=teamDAO.selectByTeamId(teamId);
        SystemNotificationDO systemNotificationDO=new SystemNotificationDO();
        //邀请成功之后给邀请人发通知,确认是否接受邀请
        if (!openId.equals(studentDO.getOpenId())){
            systemNotificationDO.setNoteType(NotificationEnum.SYSTEM_NOTE_INVITATION.name());
            systemNotificationDO.setMessage(teamDO.getName()+SYSTEM_NOTIFICATION_SUFFIX);
            notificationDAO.insertSystemNote(systemNotificationDO);
            Integer notificationId=systemNotificationDO.getId();
            notificationService.sendNotification(openId,studentDO.getOpenId(), NotificationEnum.SYSTEM_NOTE_INVITATION,notificationId,teamDO.getName()+SYSTEM_NOTIFICATION_SUFFIX,teamId.toString());
        }
        return RespUtil.success();
    }
}
