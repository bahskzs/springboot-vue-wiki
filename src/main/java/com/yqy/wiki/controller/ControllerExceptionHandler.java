package com.yqy.wiki.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.yqy.wiki.exception.BusinessException;
import com.yqy.wiki.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author bahsk
 * @createTime 2021-06-30 19:19
 * @description 统一异常处理、数据预处理等
 */

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

     /**
      * @author: bahsk
      * @date: 2021/6/30 19:30
      * @description:  捕获绑定数据异常,将success = false,报错信息返回给message
      * @params:
      * @return: CommonResp
      */
     @ExceptionHandler(value = BindException.class)
     @ResponseBody
     public CommonResp validExceptionHandler(BindException e) {
         CommonResp commonResp = new CommonResp();
         LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
         commonResp.setSuccess(false);
         commonResp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
         return commonResp;
     }


    /**
     * @author: bahsk
     * @date: 2021/7/26 16:32
     * @description: 由于User中不允许登录用户名重复，所以拦截MySQL相关提示并返回-->改造成业务异常统一拦截
     * @params:
     * @return:
     */
    @ExceptionHandler(value = MySQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public CommonResp validExceptionHandler(MySQLIntegrityConstraintViolationException e) {
        CommonResp commonResp = new CommonResp();
        LOG.warn("参数校验失败：{}", e.getLocalizedMessage());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getMessage());
        return commonResp;
    }

    /**
     * @author: bahsk
     * @date: 2021/7/26 17:16
     * @description: 业务异常统一拦截
     * @params:
     * @return:
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResp validExceptionHandler(BusinessException e) {
        CommonResp commonResp = new CommonResp();
        LOG.warn("业务异常：{}", e.getCode());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getMessage());
        return commonResp;
    }


    /**
     * @author: bahsk
     * @date: 2021/7/26 17:16
     * @description: 系统异常统一拦截
     * @params:
     * @return:
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp validExceptionHandler(Exception e) {
        CommonResp commonResp = new CommonResp();
        LOG.warn("系统异常：", e);
        commonResp.setSuccess(false);
        commonResp.setMessage("系统异常,请联系管理员");
        return commonResp;
    }


}
