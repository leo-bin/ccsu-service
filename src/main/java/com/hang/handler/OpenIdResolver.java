package com.hang.handler;

import com.hang.annotation.OpenId;
import com.hang.constant.WxConstant;
import com.hang.pojo.data.UserInfoDO;
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

    /**
     * 参数是否可以使用解析器
     *
     * @param methodParameter spring对被注解修饰过参数的包装，从其中能拿到参数的反射相关信息
     * @return
     * @apiNote 传入一个参数，用以判断此参数是否能够使用该解析器
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //只有被@OpenId 注解修饰的参数才能被此解析器处理
        return methodParameter.getParameterAnnotation(OpenId.class) != null;
    }

    /**
     * 具体解析函数
     *
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     * @apiNote 前端通过传递一个sessionId，这里的解析函数通过sessionId到redis中的用户会话信息去寻找用户信息
     */
    @Override
    public String resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String result = "";
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        Map<String, String> parse = HeaderParser.parse(request);
        String sessionId = parse.get("sessionId");
        if (StringUtils.equals(sessionId, WxConstant.TEST_SESSION_ID)) {
            // 测试
            return WxConstant.TEST_OPEN_ID;
        }
        UserInfoDO userInfo = sessionService.getSessionInfo(sessionId);
        if (userInfo != null) {
            log.info("body: " + userInfo.toString());
            result = userInfo.getOpenId();
        }
        return result;
    }
}
