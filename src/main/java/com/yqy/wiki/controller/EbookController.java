package com.yqy.wiki.controller;

import com.yqy.wiki.domain.Ebook;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.service.EBookService;
import com.yqy.wiki.vo.EbookVO;
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
    //为什么参数是ebookVO ，输入http://localhost:9520/wiki/ebook/list?name=Spring name属性也会被识别？
    //Spring会自动将参数映射到类属性
    //这是spring的什么特性？
    public CommonResp list(EbookVO ebookVO) {
        CommonResp<PageResp<EbookVO>> listCommonResp = new CommonResp<>();
        PageResp<EbookVO> list = eBookService.list(ebookVO);
        listCommonResp.setContent(list);
        return listCommonResp;
    }
}
