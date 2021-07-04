package com.yqy.wiki.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author bahsk
 * @createTime 2021-06-24 19:27
 * @description  过滤器记录请求的日志
 */

//@Component
public class LogFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOG.info("----------------LogFilter--------------------");
        long startTime = System.currentTimeMillis();
        LOG.info("远程地址:  {}" , request.getRemoteAddr());
        LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("请求参数: {}" , request.getQueryString());
        filterChain.doFilter(servletRequest, servletResponse);
        LOG.info("--LogFilter end--  花费时长:  {} ms" , System.currentTimeMillis() - startTime);

    }
}
