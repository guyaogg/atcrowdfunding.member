package com.atguigu.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author guyao
 */

@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加地址转发视图
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 浏览器访问的地址
        // 目标视图的名称,将来拼接前缀：classpath:/templates/，后缀：.html
        // 注册页跳转
        // 登录页跳转
        registry.addViewController("/agree/protocol/page").setViewName("member-start");
        registry.addViewController("/initiate/page").setViewName("member-start-step");
        registry.addViewController("/return/info/page").setViewName("project-return");
        registry.addViewController("/return/confirm/page").setViewName("project-confirm");
        registry.addViewController("/create/success/page").setViewName("project-success");
    }
}
