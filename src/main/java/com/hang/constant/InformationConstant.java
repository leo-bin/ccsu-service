package com.hang.constant;

import com.google.common.collect.Maps;

import java.util.LinkedHashMap;

/**
 * @author test
 * @date 19-4-26
 * *****************
 * function:
 */
public class InformationConstant {

    public static final LinkedHashMap<String, String> CATEGORY_MAP = Maps.newLinkedHashMap();

    public static final String INFORMATION_HOT_PREFIX = "INFORMATION_HOT_PREFIX";

    public static final String INFORMATION_PREFIX = "INFORMATION_PREFIX::";

    public static final String SYSTEM_NOTIFICATION_SUFFIX="邀请你参加他们的团队，请及时完善你的个人信息";

    public static final String INVITATION_SUCCESS_SUFFIX="恭喜你，邀请成功";

    public static final String AUTHORIZE_SUCCESS="恭喜你，你已成功被授权为团队管理员，赶快邀请成员进来吧！";

    public static final int INFORMATION_EXPIRE = 0;

    static {
        CATEGORY_MAP.put("LATEST", "最新");
        CATEGORY_MAP.put("HOT", "最热");
        CATEGORY_MAP.put("TECHNOLOGY", "技术");
        CATEGORY_MAP.put("RECRUITMENT", "招新");
        CATEGORY_MAP.put("PUBLIC", "公示");
        CATEGORY_MAP.put("NOTIFICATION", "通知");
    }
}
