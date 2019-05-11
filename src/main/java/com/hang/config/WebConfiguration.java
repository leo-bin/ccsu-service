package com.hang.config;

import com.hang.CcsuServiceApplication;
import com.hang.handler.OpenIdResolver;
import com.hang.interceptors.HeaderCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author hangs.zhang
 * @date 18-7-1
 * *****************
 * @function: 继承了该类之后 所有web配置自动失效
 */
@Configuration
@ComponentScan(basePackageClasses = CcsuServiceApplication.class, useDefaultFilters = true)
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private OpenIdResolver openIdResolver;

    @Autowired
    private HeaderCheckInterceptor headerCheckInterceptor;

    /**
     * 配置web静态资源位置
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        /// registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerCheckInterceptor)
                // 拦截
                .addPathPatterns("/feed/**")
                .addPathPatterns("/information/**")
                .addPathPatterns("/team/**")
                .addPathPatterns("/project/**")
                .addPathPatterns("/user/**")
                .addPathPatterns("/session/**")
                .addPathPatterns("/shcool/**")
                .addPathPatterns("/api/**")
                // 不拦截
                .excludePathPatterns("/user/getUserInfoByOpenId")
                .excludePathPatterns("/information/listApply")
                .excludePathPatterns("/user/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(openIdResolver);
    }

}
