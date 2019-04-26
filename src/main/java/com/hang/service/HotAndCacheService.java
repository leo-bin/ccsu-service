package com.hang.service;

import com.google.common.collect.Lists;
import com.hang.dao.InformationDAO;
import com.hang.pojo.data.InformationDO;
import com.hang.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hang.constant.InformationConstant.*;


/**
 * @author hangs.zhang
 * @date 2019/1/29
 * *****************
 * function:
 */
@Service
public class HotAndCacheService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private InformationDAO informationDAO;

    public InformationDO getInformationFromCacheById(int id) {
        InformationDO information = (InformationDO) redisUtil.get(INFORMATION_PREFIX + id);
        if (information == null) {
            information = informationDAO.selectById(id);
            addInformation2Cache(information);
        }
        return information;
    }

    public void addInformation2Cache(InformationDO information) {
        if (information != null) {
            redisUtil.set(INFORMATION_PREFIX + information.getId(), information);
        }
    }

    public void removeInformationFromCache(int id) {
        redisUtil.del(INFORMATION_PREFIX + id);
    }

    public void addInformationClick(int id) {
        redisUtil.zIncrby(INFORMATION_HOT_PREFIX, id, 1);
    }

    public List<InformationDO> getHotInformation() {
        Set<Object> set = redisUtil.zRevrange(INFORMATION_HOT_PREFIX, 0, 10);
        if (set != null) {
            return set.stream()
                    .map(e -> getInformationFromCacheById((Integer) e))
                    .collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

}
