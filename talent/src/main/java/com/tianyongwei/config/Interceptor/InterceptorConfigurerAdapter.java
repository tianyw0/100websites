package com.tianyongwei.config.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 添加路径
        // excludePathPatterns 排除路径
        registry.addInterceptor(new PermissionInterceptor())//添加登录验证拦截器
                .excludePathPatterns("/user/*")//登录注册等不需要登录权限
                .excludePathPatterns("/")//首页
                .addPathPatterns("/**");//
        super.addInterceptors(registry);
    }
}
