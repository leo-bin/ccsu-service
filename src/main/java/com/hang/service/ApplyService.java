package com.hang.service;

import com.hang.dao.ApplyDAO;
import com.hang.enums.ApplyStatusEnum;
import com.hang.enums.InformationCategoryEnum;
import com.hang.exceptions.GlobalException;
import com.hang.pojo.data.InformationApplyDO;
import com.hang.pojo.data.InformationDO;
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
            throw new GlobalException(-1, "information不存在");
        }
        if (!InformationCategoryEnum.ACTIVITY.name().equals(information.getCategory())) {
            throw new GlobalException(-1, "information类型错误");
        }
        InformationApplyDO apply = new InformationApplyDO();
        apply.setOpenId(openId);
        apply.setInformationId(informationId);
        apply.setStatus(ApplyStatusEnum.CURRENT_APPLY.name());
        int i = applyDAO.insert(apply);
        if (i != 1) {
            throw new GlobalException(-1, "报名失败，无法重复报名");
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
            throw new GlobalException(-1, "information不存在");
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
            throw new GlobalException(-1, "更新失败");
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

}
