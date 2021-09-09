package com.atguigu.crowd.filter;

import com.atguigu.crowd.constant.AccessPassResources;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录检查过滤器,继承抽象类ZuulFilter（网关过滤器
 *
 * @author guyao
 */
@Slf4j
@Component
public class CrowdAccessFilter extends ZuulFilter {

    @Override
    public String filterType() {
        // 在微服务前执行
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 获取requestContext对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        // 通过对象获取request对象（框架底层使用ThreadLocal（线程本地化，同一线程）实现绑定到线程上
        HttpServletRequest request = currentContext.getRequest();
        // 获取请求路径

        String servletPath = request.getServletPath();

        // 根据servletPath判断是否是登录功能（直接放行
        boolean pass = AccessPassResources.PASS_RES_SET.contains(servletPath);

        log.info("放行资源检查:" + servletPath);
        if (pass) {
            // 如果是放行路径放行
            return false;
        }
        boolean contains = servletPath.contains(AccessPassResources.PROJECT_DETAIL);
        if (contains) {
            // 项目详情展示放行
            return false;
        }
        // 判断是否为静态资源
        log.info("静态资源检查:" + servletPath);
        return !AccessPassResources.judgeCurrentServletPathWhetherStaticResource(servletPath);

    }

    @Override
    public Object run() throws ZuulException {

        // 获取当前请求对象
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        // 获取当前session对象
        HttpSession session = request.getSession();
        // 尝试获取登录用户
        MemberLoginVO loginMember = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER.getStr());
        log.info(String.valueOf(loginMember));
        // 判断loginMember是否为kong
        if (loginMember == null) {
            // 获取响应
            HttpServletResponse response = currentContext.getResponse();
            // 将提示信息存入session
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE.getStr(), CrowdConstant.MESSAGE_ACCESS_FORBIDDEN.getStr());
            String interceptPath = request.getRequestURL().toString();
            // 没有放行将拦截请求放入session方便登录后跳转
            session.setAttribute(CrowdConstant.ATTR_NAME_INTERCEPT.getStr(), interceptPath);
            // 重定向到auth-consumer微服务的登录页面
            try {
                response.sendRedirect("http://101.132.45.198/crowd/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 返回值可以无视
        return null;
    }
}
