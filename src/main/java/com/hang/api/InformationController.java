package com.hang.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.hang.aop.StatisticsTime;
import com.hang.exceptions.ApiException;
import com.hang.pojo.data.InformationDO;
import com.hang.pojo.vo.BaseRes;
import com.hang.service.HotAndCacheService;
import com.hang.service.InformationService;
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
    private HotAndCacheService hotAndCacheService;

    /**
     * 列表类目list
     */
    @StatisticsTime("categoryList")
    @ApiOperation("请求类别列表")
    @GetMapping("/categoryList")
    public BaseRes categoryList() {
        return RespUtil.success(CATEGORY_MAP);
    }

    /**
     * 创建
     */
    @StatisticsTime("createInformation")
    @ApiOperation("请求一个information")
    @PostMapping("/createInformation")
    public BaseRes createInformation(@RequestParam String title, @RequestParam String content,
                                     @RequestParam String authors, @RequestParam String category,
                                     @RequestParam String notes) {
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
        information.setNotes(notes);
        informationService.addInformation(information);
        return RespUtil.success();
    }

    /**
     * 删除
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
     */
    @StatisticsTime("getInformationById")
    @ApiOperation("根据id获取information")
    @JsonView(InformationDO.DetailInformation.class)
    @GetMapping("/getInformationById")
    public BaseRes getInformationById(@RequestParam int id) {
        return RespUtil.success(informationService.getInformationById(id));
    }


    @StatisticsTime("listAll")
    @GetMapping("/listAll")
    public BaseRes listAll(@RequestParam(required = false, defaultValue = "0") int start,
                           @RequestParam(required = false, defaultValue = "100") int offset) {
        return RespUtil.success(informationService.list(start, offset));
    }

    /**
     * 更新redis缓存信息
     */
    @PostMapping("/updateCacheInfo")
    public BaseRes updateCacheInfo(int id) {
        hotAndCacheService.updateInformation(id);
        return RespUtil.success();
    }
}
