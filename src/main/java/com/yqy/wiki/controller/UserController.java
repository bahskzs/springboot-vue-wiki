package com.yqy.wiki.controller;

import com.yqy.wiki.req.EbookSaveReq;
import com.yqy.wiki.req.UserQueryReq;
import com.yqy.wiki.req.UserSaveReq;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.UserQueryResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author bahsk
 * @createTime 2021-07-26 14:53
 * @description
 */
@RestController
@RequestMapping("/user")
public class UserController {

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
        CommonResp commonResp = userService.save(userSaveReq);
        return commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp commonResp = userService.delete(id);
        return commonResp;
    }
}
