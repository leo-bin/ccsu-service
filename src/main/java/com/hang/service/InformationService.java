package com.hang.service;

import com.google.common.collect.Lists;
import com.hang.dao.InformationDAO;
import com.hang.exceptions.ApiException;
import com.hang.pojo.data.InformationDO;
import com.hang.pojo.vo.MyInformationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hangs.zhang
 * @date 2019/1/28
 * *****************
 * function:
 */
@Service
public class InformationService {

    @Autowired
    private HotAndCacheService cacheService;

    @Autowired
    private InformationDAO informationDAO;

    /**
     * 创建,创建之后返回id
     *
     * @param information
     * @return
     */
    public void addInformation(InformationDO information) {
        int i = informationDAO.insert(information);
        if (i != 1) {
            throw new ApiException(-1, "添加失败");
        }
        cacheService.addInformation2Cache(information);
        return;
    }
    /**
     * 删除
     *
     * @param id
     */
    public void removeInformation(int id) {
        int i = informationDAO.delete(id);
        if (i != 1) {
            throw new ApiException(-1, "删除失败");
        }
        cacheService.removeInformationFromCache(id);
    }

    /**
     * 修改
     *
     * @param information
     */
    public void modifyInformation(InformationDO information) {
        int i = informationDAO.update(information);
        if (i != 1) {
            throw new ApiException(-1, "修改失败");
        }
        cacheService.addInformation2Cache(information);
    }

    /**
     * 查询指定id区域的数据
     *
     * @param start
     * @param offset
     * @return
     */
    public List<InformationDO> list(int start, int offset) {
        return informationDAO.list(start, offset);
    }

    /**
     * 查询information数量
     *
     * @return
     */
    public int count() {
        return informationDAO.count();
    }

    /**
     * 查询指定id的数据
     *
     * @param id
     * @return
     */
    public InformationDO getInformationById(int id) {
        InformationDO information = cacheService.getInformationFromCacheById(id);
        if (information != null) {
            cacheService.addInformationClick(id);
        }
        return information;
    }

    /**
     * 查询最新数据 10条
     * @apiNote 只是招新和通知
     *
     * @return
     */
    public List<InformationDO> getLatestInformation(Integer start,Integer offset) {
        ArrayList<InformationDO> list = informationDAO.listNoteAndRecruitment(start,offset);
        return list;
    }

    /**
     * 根据类别查询
     *
     * @param category
     * @param start
     * @param offset
     * @return
     */
    public List<InformationDO> getInformationByCategory(String category, int start, int offset) {
        return informationDAO.listByCategory(category, start, offset);
    }

    /**
     * 查询热榜 10个
     *
     * @return
     */
    public List<InformationDO> getHotInformation() {
        return cacheService.getHotInformation();
    }
}
