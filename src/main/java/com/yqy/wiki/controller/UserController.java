package com.yqy.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.yqy.wiki.req.*;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.UserLoginResp;
import com.yqy.wiki.resp.UserQueryResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.service.UserService;
import com.yqy.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @author bahsk
 * @createTime 2021-07-26 14:53
 * @description
 */
@RestController
@RequestMapping("/user")
public class UserController {


    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @GetMapping("/list")
    //为什么参数是userVO ，输入http://localhost:9520/wiki/user/list?name=Spring name属性也会被识别？
    //Spring会自动将参数映射到类属性
    //这是spring的什么特性？
    public CommonResp list(@Valid UserQueryReq userQueryVO) {
        CommonResp<PageResp<UserQueryResp>> listCommonResp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(userQueryVO);
        listCommonResp.setContent(list);
        return listCommonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq userSaveReq) {
        userSaveReq.setPassword(DigestUtils.md5DigestAsHex(userSaveReq.getPassword().getBytes()));
        CommonResp commonResp = userService.save(userSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp commonResp = userService.delete(id);
        return commonResp;
    }

    /**
     * @author: bahsk
     * @date: 2021/7/26 20:24
     * @description: 重置密码
     * @params:
     * @return:
     */
    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp commonResp = new CommonResp<>();
        userService.resetPassword(req);
        return commonResp;
    }


    @PostMapping("/login")
    public CommonResp resetPassword(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> commonResp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        Long token = snowFlake.nextId();

        LOG.info("生成单点登录token：{}，并放入redis中", token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);

        commonResp.setContent(userLoginResp);
        return commonResp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token) {
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        LOG.info("从redis中删除token{}", token);
        return resp;

    }
}
