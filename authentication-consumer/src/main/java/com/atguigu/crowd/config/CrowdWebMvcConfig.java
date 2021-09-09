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
        String urlPath = "/auth/member/to/reg/page";
        // 目标视图的名称,将来拼接前缀：classpath:/templates/，后缀：.html
        String viewName = "member-reg";
        // 注册页跳转
        registry.addViewController(urlPath).setViewName(viewName);
        // 登录页跳转
        registry.addViewController("/auth/member/to/login/page").setViewName("member-login");
        registry.addViewController("/auth/member/to/center/page").setViewName("member-center");
        registry.addViewController("/member/my/crowd").setViewName("member-crowd");
    }
}
