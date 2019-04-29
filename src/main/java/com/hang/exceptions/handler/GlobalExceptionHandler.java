package com.hang.exceptions.handler;

import com.hang.enums.ResultEnum;
import com.hang.exceptions.ApiException;
import com.hang.pojo.vo.BaseRes;
import com.hang.utils.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author hangs.zhang
 * @date 2018/11/20
 * *****************
 * function:
 * 当发生异常之后，如果在本类中找不到@ExceptionHandler修饰的方法进行异常处理时
 * 就会寻找@ControllerAdvice修饰的类中的@ExceptionHandler修饰的方法进行异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public BaseRes exceptionHandler(Exception ex, HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 打印发生异常的请求参数
        parameterMap.forEach((key, value) -> log.error("error param {}:{}", key, value));
        // 打印请求的路径，可以根据这个路径去在监控中mark
        log.error("RequestURI {}", request.getRequestURI());
        // 打印异常堆栈
        log.error("exception", ex);

        // 自定义的感兴趣的异常
        if (ex instanceof ApiException) {
            ApiException globalException = (ApiException) ex;
            return RespUtil.error(globalException.getCode(), globalException.getMessage());
            // 其他异常
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            return RespUtil.error(-1, "对不起，不支持当前HTTP方法");

        } else if (ex instanceof MissingServletRequestParameterException) {
            return RespUtil.error(-1, ex.getMessage());
        } else {
            return RespUtil.error(ResultEnum.SERVER_INNER_ERROR);
        }
    }


    private String list2String(List<String> list) {
        String prefix = "validate error : ";
        StringBuilder content = new StringBuilder();
        for (int i = 0, length = list.size(); i < length; i++) {
            String str = list.get(i);
            if (i == length - 1) {
                content.append(str);
            } else {
                content.append(str).append(", ");
            }
        }
        return prefix + content.toString();
    }

}
