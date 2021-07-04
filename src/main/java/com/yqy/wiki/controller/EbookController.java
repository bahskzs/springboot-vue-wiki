package com.yqy.wiki.controller;

import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.EbookQueryResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.service.EBookService;
import com.yqy.wiki.req.EbookQueryReq;
import com.yqy.wiki.req.EbookSaveReq;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    //为什么参数是ebookVO ，输入http://localhost:9520/wiki/ebook/list?name=Spring name属性也会被识别？
    //Spring会自动将参数映射到类属性
    //这是spring的什么特性？
    public CommonResp list(@Valid EbookQueryReq ebookQueryVO) {
        CommonResp<PageResp<EbookQueryResp>> listCommonResp = new CommonResp<>();
        PageResp<EbookQueryResp> list = eBookService.list(ebookQueryVO);
        listCommonResp.setContent(list);
        return listCommonResp;
    }


    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq ebookSaveReq){
        CommonResp commonResp = eBookService.save(ebookSaveReq);
        return  commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp commonResp = eBookService.delete(id);
        return  commonResp;
    }


}
