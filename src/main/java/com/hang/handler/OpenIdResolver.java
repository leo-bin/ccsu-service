package com.hang.handler;

import com.hang.annotation.OpenId;
import com.hang.constant.WxConstant;
import com.hang.pojo.data.UserInfoDO;
import com.hang.pojo.vo.BaseRes;
import com.hang.service.SessionService;
import com.hang.utils.HeaderParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author hangs.zhang
 * @date 2018/12/13
 * *****************
 * function:
 */
@Slf4j
@Component
public class OpenIdResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private SessionService sessionService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(OpenId.class) != null;
    }

    @Override
    public String resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String result = "";
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        Map<String, String> parse = HeaderParser.parse(request);
        String sessionId = parse.get("sessionId");
        if (StringUtils.isEmpty(sessionId)) {
            // 测试
            return WxConstant.TEST_OPEN_ID;
        }

        BaseRes sessionInfo = sessionService.getSessionInfo(sessionId);
        UserInfoDO userInfo = (UserInfoDO) sessionInfo.getData();
        if (userInfo != null) {
            log.info("body: " + userInfo.toString());
            result = userInfo.getOpenId();
        } else {
            // 测试
            result = WxConstant.TEST_OPEN_ID;
        }
        return result;
    }
}
