package com.yqy.wiki.controller;

import com.yqy.wiki.domain.Ebook;
import com.yqy.wiki.service.EBookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author bahsk
 * @createTime 2021-06-18 0:36
 * @description
 */

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EBookService eBookService;

    @GetMapping("/list")
    public List<Ebook> list() {
        return eBookService.list();
    }
}
