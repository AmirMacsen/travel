package com.example.travel.interceptor;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class InterceptorConfig implements WebMvcConfigurer {
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        //创建拦截器对象并指定其拦截的路径
        registry.addInterceptor(new MemberInterceptor())
                .addPathPatterns("/frontdesk/favorite/**");
    }
}
