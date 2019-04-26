/*
 * Created by Long Duping
 * Date 2018/12/6 14:18
 */
package com.hang.utils;


import com.hang.pojo.dto.RequestHeaderDTO;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析统一请求头
 *
 * @author test
 */
public class HeaderParser {

    public static Map<String, String> parse(HttpServletRequest request) throws ClassNotFoundException {
        Map<String, String> map = new HashMap<>(16);
        parse(RequestHeaderDTO.class, "", request, map);
        return map.size() > 0 ? map : null;
    }

    public static Map<String, String> parse(Class clz, String prefix, HttpServletRequest request, Map<String, String> map) throws ClassNotFoundException {
        Field[] fields = clz.getDeclaredFields();
        String key = null, value;
        for (Field f : fields) {
            if (isBaseType(f.getType())) {
                // 字符串类型
                key = prefix + f.getName();
            } else {
                // 自定义类型
                parse(Class.forName(f.getType().getName()), f.getName() + ".", request, map);
            }
            value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public static boolean isBaseType(Class<?> clz) {

        if (clz.equals(Integer.class) ||
                clz.equals(int.class) ||
                clz.equals(Byte.class) ||
                clz.equals(byte.class) ||
                clz.equals(Long.class) ||
                clz.equals(long.class) ||
                clz.equals(Double.class) ||
                clz.equals(double.class) ||
                clz.equals(Float.class) ||
                clz.equals(float.class) ||
                clz.equals(Character.class) ||
                clz.equals(char.class) ||
                clz.equals(Short.class) ||
                clz.equals(short.class) ||
                clz.equals(Boolean.class) ||
                clz.equals(boolean.class) || clz.equals(boolean.class) ||
                clz.equals(String.class) ||
                clz.equals(java.util.Date.class) ||
                clz.equals(java.sql.Date.class)) {
            return true;
        }
        return false;
    }
}
