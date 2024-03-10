package com.tianyongwei.config.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 要想让过滤器生效必须加上@Component，加入spring管理
 */
@WebFilter(filterName="XssFilter",urlPatterns = "/*")
@Component
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        chain.doFilter(new XssHttpservletRequestWrapper(httpServletRequest),response);
    }

    @Override
    public void destroy() {
    }
}
