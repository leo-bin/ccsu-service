package com.hang.aop;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

/**
 * @author hangs.zhang
 * @date 2018/7/25
 * *********************
 * function:
 */
@Aspect
@Component
@Slf4j
public class StatisticsAdvice {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        logger.info("statistics advice init");
    }

    @AfterThrowing(value = "@annotation(statisticsTime)")
    public void afterThrow(StatisticsTime statisticsTime) {
        String monitorName = statisticsTime.value();
        logger.error("{} error", monitorName);
        /// Metrics.meter(monitorName + ".meter").get().mark();
    }

    @Around("@annotation(statisticsTime)")
    @ResponseBody
    public Object statisticsTime(ProceedingJoinPoint pjp, StatisticsTime statisticsTime) throws Throwable {
        /// Timer.Context context = null;
        String monitorName = null;
        if (Objects.nonNull(statisticsTime)) {
            monitorName = statisticsTime.value();
            /// context = Metrics.timer(monitorName + ".timer").get().time();
        } else {
            logger.error("can^t get statistics anno error");
        }
        printMethodNameAndArgs(pjp);
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = pjp.proceed();
        } finally {
            logger.info("monitor : {}, result : {}", monitorName, JSON.toJSONString(result));
            logger.info(monitorName + " cost time : " + (System.currentTimeMillis() - startTime));
        }
        return result;
    }

    /**
     * 获取拦截的方法与参数名，以及传入的参数
     */
    private static void printMethodNameAndArgs(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = pjp.getArgs();
        Map<String, Object> map = Maps.newHashMap();
        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], args[i]);
        }
        String format = "###class:%s###method:%s###args:%s###";
        log.info(String.format(format, pjp.getTarget().getClass().getName(), pjp.getSignature().getName(), JSON.toJSONString(map)));
    }

}
