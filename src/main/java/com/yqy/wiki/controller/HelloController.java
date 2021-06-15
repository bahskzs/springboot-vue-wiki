package com.yqy.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bahsk
 * @createTime 2021-06-15 20:13
 * @description
 */

@RestController
public class HelloController {

    //有则取demo,没有则取默认值DEMO
    @Value("${hello:DEMO}")
    private String demo;

    @GetMapping("/hello")
    public String hello(){
        return "hello" + demo;
    }
}
