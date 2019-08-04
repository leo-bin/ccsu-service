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

    public static final int INFORMATION_EXPIRE = 0;

    static {
        CATEGORY_MAP.put("LATEST", "最新");
        CATEGORY_MAP.put("HOT", "最热");
        CATEGORY_MAP.put("TECHNOLOGY", "技术");
        CATEGORY_MAP.put("NOTIFICATION", "通知");
        CATEGORY_MAP.put("RECRUITMENT", "招新");
        CATEGORY_MAP.put("PUBLIC", "公示");
    }

}
