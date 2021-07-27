package com.yqy.wiki.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * @author bahsk
 * @createTime 2021-06-24 20:01
 * @description
 */

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoginInterceptor.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LOG.info("----------------LoginInterceptor--------------------");
//        String token = request.getHeader("tokens");
        String token = request.getHeader("token");
        String accept = request.getHeader("Accept");
        LOG.info("登录校验开始, Accept: {}", accept);
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        LOG.info("登录校验开始, token: {}", token);
        //判空
        if (token == null || token.isEmpty()) {
            LOG.info("token为空, 请求被拦截");
            //401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        //非空则确定是否合法
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj == null) {
            LOG.warn("token无效, 请求被拦截");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        } else {
            LOG.info("已登录: {}", obj);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {


    }
}
