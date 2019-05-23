package com.hang.service;

import com.google.common.collect.Lists;
import com.hang.dao.ApplyDAO;
import com.hang.enums.ApplyStatusEnum;
import com.hang.enums.InformationCategoryEnum;
import com.hang.exceptions.ApiException;
import com.hang.pojo.data.InformationApplyDO;
import com.hang.pojo.data.InformationDO;
import com.hang.pojo.data.UserInfoDO;
import com.hang.pojo.vo.ApplyMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hangs.zhang
 * @date 2019/1/30
 * *****************
 * function:
 */
@Service
public class ApplyService {

    @Autowired
    private ApplyDAO applyDAO;

    @Autowired
    private InformationService informationService;

    @Autowired
    private UserService userService;

    /**
     * 报名
     *
     * @param informationId
     * @param openId
     */
    @Transactional(rollbackFor = Exception.class)
    public void apply(int informationId, String openId) {
        InformationDO information = informationService.getInformationById(informationId);
        if (information == null) {
            throw new ApiException(-1, "information不存在");
        }
        if (!InformationCategoryEnum.ACTIVITY.name().equals(information.getCategory())) {
            throw new ApiException(-1, "information类型错误");
        }
        InformationApplyDO apply = new InformationApplyDO();
        apply.setOpenId(openId);
        apply.setInformationId(informationId);
        apply.setStatus(ApplyStatusEnum.CURRENT_APPLY.name());
        int i = applyDAO.insert(apply);
        if (i != 1) {
            throw new ApiException(-1, "报名失败，无法重复报名");
        }
    }

    /**
     * 更新报名状态
     *
     * @param applyId
     * @param applyStatus
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateApplyStatus(int applyId, ApplyStatusEnum applyStatus) {
        InformationApplyDO apply = applyDAO.selectById(applyId);
        // TODO: 2019/1/30 将id转化为数据
        String openId = apply.getOpenId();


        int informationId = apply.getInformationId();
        InformationDO information = informationService.getInformationById(informationId);
        if (information == null) {
            throw new ApiException(-1, "information不存在");
        }
        String content;
        if (applyStatus.equals(ApplyStatusEnum.FAILURE)) {
            content = "非常抱歉，当前活动报名失败";
        } else {
            content = "恭喜您，活动报名成功";
        }
        // TODO: 19-4-26 发送异步消息 websocket推送前端

        int i = applyDAO.update(applyId, applyStatus.name());
        if (i != 1) {
            throw new ApiException(-1, "更新失败");
        }
    }

    /**
     * 查看对应information的报名列表
     *
     * @param informationId
     * @return
     */
    public List<InformationApplyDO> getApplyByInformationId(int informationId) {
        return applyDAO.listAppliesByInformationId(informationId);
    }

    public List<ApplyMessageVO> getAllApply(int start, int offset) {
        List<ApplyMessageVO> result = Lists.newArrayList();
        List<InformationApplyDO> applyDOS = applyDAO.list(start, offset);
        applyDOS.forEach(e -> {
            ApplyMessageVO applyMessageVO = new ApplyMessageVO();
            InformationDO information = informationService.getInformationById(e.getInformationId());
            if (information == null) return;
            applyMessageVO.setActivityName(information.getTitle());

            UserInfoDO userInfo = userService.getUserInfoByOpenId(e.getOpenId());
            applyMessageVO.setJwcAccount(userInfo.getJwcAccount());
            applyMessageVO.setNickName(userInfo.getNickName());
            applyMessageVO.setOpenId(userInfo.getOpenId());

            applyMessageVO.setApplyId(e.getApplyId());
            applyMessageVO.setStatus(e.getStatus());
            applyMessageVO.setStatusMessage(ApplyStatusEnum.getByName(e.getStatus()).getMessgae());
            result.add(applyMessageVO);
        });
        return result;
    }

    public List<InformationApplyDO> getInformationApplyByOpenId(String openId) {
        return applyDAO.selectByOpenId(openId);
    }

}
