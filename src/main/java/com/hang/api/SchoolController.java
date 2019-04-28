package com.hang.api;

import com.hang.annotation.OpenId;
import com.hang.enums.ResultEnum;
import com.hang.exceptions.GlobalException;
import com.hang.pojo.data.CourseDO;
import com.hang.pojo.data.UserInfoDO;
import com.hang.pojo.vo.BaseRes;
import com.hang.pojo.vo.CourseVO;
import com.hang.service.SchoolService;
import com.hang.service.UserService;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author test
 * @date 19-4-28
 * *****************
 * function:
 */
@Api("校园服务")
@RestController
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private UserService userService;

    @ApiOperation("查询第n周的课表,OpenId参数不用传")
    @GetMapping("/course/getCourseByWeek")
    public BaseRes getCourseByWeek(@OpenId String openId, @RequestParam Integer week,
                                   @RequestParam(required = false, defaultValue = "2018-2019-2") String semester) {
        openIdCheck(openId);
        UserInfoDO userInfo = userService.getUserInfoByOpenId(openId);
        jwcAccountCheck(userInfo);

        List<CourseVO> courseVOS = schoolService.getCourseByWeek(userInfo.getJwcAccount(), week, semester);
        return RespUtil.success(courseVOS);
    }

    @ApiOperation("查询全部课表,OpenId参数不用传")
    @GetMapping("/course/getAllCourse")
    public BaseRes getAllCourse(@OpenId String openId,
                                @RequestParam(required = false, defaultValue = "2018-2019-2") String semester) {
        openIdCheck(openId);
        UserInfoDO userInfo = userService.getUserInfoByOpenId(openId);
        jwcAccountCheck(userInfo);

        List<CourseDO> allCourse = schoolService.getAllCourse(userInfo.getJwcAccount(), semester);
        return RespUtil.success(allCourse);
    }

    private void openIdCheck(String openId) {
        if (StringUtils.isEmpty(openId)) {
            throw new GlobalException(ResultEnum.CAN_NOT_GET_OPEN_ID);
        }
    }

    private void jwcAccountCheck(UserInfoDO userInfo) {
        if (Objects.isNull(userInfo)) {
            throw new GlobalException(ResultEnum.CAN_NOT_GET_USER_INFO);
        } else if (StringUtils.isEmpty(userInfo.getJwcAccount())) {
            throw new GlobalException(ResultEnum.JWC_ACCOUNT_NOT_BIND);
        }
    }

}
