package com.tianyongwei.config.Interceptor;

import com.tianyongwei.config.MyWebUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 参考文章：
 * 1、csdn: http://blog.csdn.net/gfd54gd5f46/article/details/75022305
 * 2、自己的知乎回答：https://www.zhihu.com/question/35225845/answer/345131918
 */
public class PermissionInterceptor implements HandlerInterceptor {
    private String[] whiteList = {"/","/user/signin","/user/signup","/user/"};

    /**
     * 1、请求之前调用，也就是调用Controller方法之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle...");
        /**
         * 1、设置成拦截所有请求：/** (在配置中实现)
         * 2、注册，登录，退出，首页等不需要进行拦截 （）
         * 3、拦截需要登录才能看的
         * 4、拦截需要登录并且身份对应（todo）
         */
        if(MyWebUtil.getCurrentUser() == null) {
            request.getSession().setAttribute("BlockedURI",request.getRequestURI());
            response.sendRedirect("/user/signin");
            return false;
        }
        return true;//返回true则继续向下执行，返回false则取消当前请求
    }

    /**
     * 2、请求之后，视图渲染之前被调用。
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle...");
    }

    /**
     * 3、请求结束之后被调用，主要用于清理工作。
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion...");
    }
}
