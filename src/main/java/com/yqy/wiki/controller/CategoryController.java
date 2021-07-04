package com.yqy.wiki.controller;

import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.CategoryQueryResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.service.CategoryService;
import com.yqy.wiki.service.EBookService;
import com.yqy.wiki.req.CategoryQueryReq;
import com.yqy.wiki.req.CategorySaveReq;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author bahsk
 * @createTime 2021-06-18 0:36
 * @description
 */

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    //为什么参数是categoryReq ，输入http://localhost:9520/wiki/category/list?name=Spring name属性也会被识别？
    //Spring会自动将参数映射到类属性
    //这是spring的什么特性？
    public CommonResp list(@Valid  CategoryQueryReq categoryQueryReq) {
        CommonResp<PageResp<CategoryQueryResp>> listCommonResp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryQueryReq);
        listCommonResp.setContent(list);
        return listCommonResp;
    }

    @GetMapping("/all")
    public CommonResp all() {
        return categoryService.all();
    }



    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq categorySaveReq){
        CommonResp commonResp = categoryService.save(categorySaveReq);
        return  commonResp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp commonResp = categoryService.delete(id);
        return  commonResp;
    }
}
