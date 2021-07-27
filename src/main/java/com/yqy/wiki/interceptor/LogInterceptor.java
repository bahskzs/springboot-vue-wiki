//package com.yqy.wiki.interceptor;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @author bahsk
// * @createTime 2021-06-24 20:01
// * @description
// */
//
//@Component
//public class LogInterceptor implements HandlerInterceptor {
//
//    private static final Logger LOG = LoggerFactory.getLogger(LogInterceptor.class);
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        LOG.info("----------------LogInterceptor--------------------");
//        long startTime = System.currentTimeMillis();
//        LOG.info("远程地址:  {}" , request.getRemoteAddr());
//        LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
//        LOG.info("请求参数: {}" , request.getQueryString());
//        request.setAttribute("requestStatTime",startTime);
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//        long startTime = (long) request.getAttribute("requestStatTime");
//        LOG.info("--LogInterceptor end--   花费时长:  {} ms" , System.currentTimeMillis() - startTime);
//    }
//}
