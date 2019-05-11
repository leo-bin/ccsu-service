package com.hang.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.hang.annotation.OpenId;
import com.hang.aop.StatisticsTime;
import com.hang.constant.InformationConstant;
import com.hang.enums.ApplyStatusEnum;
import com.hang.enums.ResultEnum;
import com.hang.exceptions.ApiAssert;
import com.hang.exceptions.ApiException;
import com.hang.pojo.data.InformationDO;
import com.hang.pojo.vo.BaseRes;
import com.hang.service.ApplyService;
import com.hang.service.InformationService;
import com.hang.service.StudentService;
import com.hang.utils.RespUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.hang.constant.InformationConstant.CATEGORY_MAP;


/**
 * @author hangs.zhang
 * @date 2019/1/28
 * *****************
 * function:
 */
@Api("首页information服务，information即feed流中得内容")
@RequestMapping("/information")
@RestController
public class InformationController {

    @Autowired
    private InformationService informationService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private StudentService studentService;

    /**
     * 列表类目list
     *
     * @return
     */
    @StatisticsTime("categoryList")
    @ApiOperation("请求类别列表")
    @GetMapping("categoryList")
    public BaseRes categoryList() {
        return RespUtil.success(CATEGORY_MAP);
    }

    /**
     * 创建
     *
     * @param title
     * @param content
     * @param authors
     * @param category
     * @return
     */
    @StatisticsTime("createInformation")
    @ApiOperation("请求一个information")
    @PostMapping("/createInformation")
    public BaseRes createInformation(String title, String content, String authors, String category) {
        if (!CATEGORY_MAP.containsKey(category)) {
            throw new ApiException(-1, "category不存在");
        }

        InformationDO information = new InformationDO();
        information.setTitle(title);
        information.setAuthors(authors);
        information.setContent(content);
        information.setCategory(category);
        information.setCategoryName(CATEGORY_MAP.get(category));
        information.setReleaseTime(new Date());
        informationService.addInformation(information);
        return RespUtil.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @StatisticsTime("removeInformation")
    @ApiOperation("删除information")
    @PostMapping("/removeInformation")
    public BaseRes removeInformation(@RequestParam int id) {
        informationService.removeInformation(id);
        return RespUtil.success();
    }

    /**
     * 修改
     *
     * @param id
     * @param title
     * @param content
     * @param authors
     * @return
     */
    @StatisticsTime("modifyInformation")
    @ApiOperation("修改information")
    @PostMapping("/modifyInformation")
    public BaseRes modifyInformation(int id, String title, String content, String authors) {
        InformationDO information = informationService.getInformationById(id);
        if (information == null) {
            return RespUtil.error(-1, "information不存在");
        }
        if (StringUtils.isNotBlank(title)) {
            information.setTitle(title);
        }
        if (StringUtils.isNotBlank(content)) {
            information.setContent(content);
        }
        if (StringUtils.isNotBlank(authors)) {
            information.setAuthors(authors);
        }
        informationService.modifyInformation(information);
        return RespUtil.success();
    }

    /**
     * 查询information
     *
     * @param id
     * @return
     */
    @StatisticsTime("getInformationById")
    @ApiOperation("根据id获取information")
    @JsonView(InformationDO.DetailInformation.class)
    @GetMapping("/getInformationById")
    public BaseRes getInformationById(@RequestParam int id) {
        return RespUtil.success(informationService.getInformationById(id));
    }

    /**
     * activity 活动申请
     *
     * @param openId
     * @param informationId
     * @return
     */
    @StatisticsTime("activityApply")
    @ApiOperation("申请活动")
    @PostMapping("/applyActivity")
    public BaseRes activityApply(@OpenId String openId, int informationId) {
        ApiAssert.checkOpenId(openId);
        if (StringUtils.isEmpty(openId)) {
            return RespUtil.error(ResultEnum.CAN_NOT_GET_OPEN_ID);
        }

        InformationDO information = informationService.getInformationById(informationId);
        if (information == null) {
            return RespUtil.error(-1, "找不到information");
        }
        if (!CATEGORY_MAP.containsKey(information.getCategory())) {
            return RespUtil.error(-1, "不是活动，无法报名");
        }
        applyService.apply(information.getId(), openId);
        return RespUtil.success();
    }

    /**
     * 更新申请状态为成功
     *
     * @param applyId
     * @return
     */
    @StatisticsTime("modifyStatusSuccess")
    @ApiOperation("更新activity 申请状态为成功")
    @GetMapping("/modifyActivityStatusSuccess")
    public BaseRes modifyStatusSuccess(@OpenId String openId, int applyId) {
        ApiAssert.checkOpenId(openId);
        studentService.addComprehensiveFraction(openId, 1.0);
        applyService.updateApplyStatus(applyId, ApplyStatusEnum.SUCCESS);
        return RespUtil.success();
    }

    /**
     * 更新申请状态为失败
     *
     * @param applyId
     * @return
     */
    @StatisticsTime("modifyStatusFailure")
    @ApiOperation("更新activity 申请状态为失败")
    @GetMapping("/modifyActivityStatusFailure")
    public BaseRes modifyStatusFailure(int applyId) {
        applyService.updateApplyStatus(applyId, ApplyStatusEnum.FAILURE);
        return RespUtil.success();
    }


    @StatisticsTime("myActivity")
    @ApiOperation("我参加的活动")
    @GetMapping("/myActivity")
    public BaseRes myActivity(@OpenId String openId) {
        return RespUtil.success(informationService.getInformationByOpenId(openId));
    }

    /**
     * 查询申请列表
     *
     * @return
     */
    @StatisticsTime("listApply")
    @GetMapping("/listApply")
    public BaseRes listApply(@RequestParam(required = false, defaultValue = "0") int start,
                             @RequestParam(required = false, defaultValue = "10") int offset) {
        return RespUtil.success(applyService.getAllApply(start, offset));
    }

}
