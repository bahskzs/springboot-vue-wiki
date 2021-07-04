package com.yqy.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqy.wiki.domain.Category;
import com.yqy.wiki.domain.CategoryExample;
import com.yqy.wiki.mapper.CategoryMapper;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.CategoryQueryResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.util.CopyUtil;
import com.yqy.wiki.util.SnowFlake;
import com.yqy.wiki.req.CategoryQueryReq;
import com.yqy.wiki.req.CategorySaveReq;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bahsk
 * @createTime 2021-06-18 0:31
 * @description
 */
@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

     /**
      * @author: bahsk
      * @date: 2021/6/18 21:47
      * @description: 返回输入模糊匹配电子书列表
      * @params: String name
      * @return: List<Category>
      */
    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryQueryReq) {
        CategoryExample categoryExample = new CategoryExample();
        List<Category> categoryList = new ArrayList<>();

        //类似一个where子句

            PageHelper.startPage(categoryQueryReq.getPage(), categoryQueryReq.getSize());
            categoryList = categoryMapper.selectByExample(null);

        //传统写法 ： 将List<Category> 变为 List<CategoryReq> 需要用到循环 --> CopyUtil
//        List<CategoryReq> categoryReqList = new ArrayList<>();
//        for(Category category : categoryList) {
//            CategoryReq vo = new CategoryReq();
//            /* 传统写法 --> Spring提供了一个工具类 BeanUtils
//            vo.setId(category.getId());
//            vo.setName(category.getName());
//             */
//
//            BeanUtils.copyProperties(category,vo);
//
//            categoryReqList.add(vo);
//        }
        // 采用可以复制List的工具类优化
        PageResp<CategoryQueryResp> categoryReqPageResp = new PageResp<>();
        PageInfo<CategoryQueryResp> categoryReqPageInfo = new PageInfo<>((List)categoryList);

        categoryReqPageResp.setTotal(categoryReqPageInfo.getTotal());
        categoryReqPageResp.setList(CopyUtil.copyList(categoryList, CategoryQueryResp.class));
        return categoryReqPageResp;
    }

     /**
      * @author: bahsk
      * @date: 2021/6/18 21:47
      * @description: 返回电子书列表
      * @params:
      * @return:
      */
    public CommonResp<List<CategoryQueryResp>> all() {
        CommonResp<List<CategoryQueryResp>> categoryQueryRespCommonResp = new CommonResp<>();
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        categoryQueryRespCommonResp.setContent((List) categoryMapper.selectByExample(null));
        return categoryQueryRespCommonResp;
    }



     /**
      * @author: bahsk
      * @date: 2021/6/29 22:04
      * @description: 保存
      * @params:
      * @return:
      */
    public CommonResp save(CategorySaveReq categorySaveReq) {
        Category category = CopyUtil.copy(categorySaveReq, Category.class);
        CommonResp commonResp = new CommonResp();
        int flag = 0;
        if (ObjectUtils.isEmpty(category.getId())){
            category.setId(snowFlake.nextId());
            flag = categoryMapper.insert(category);
        }else{
            flag = categoryMapper.updateByPrimaryKey(category);
        }
        if(flag == 0){
            commonResp.setSuccess(false);
        }
        return commonResp;
    }

     /**
      * @author: bahsk
      * @date: 2021/6/29 23:11
      * @description: 删除
      * @params:
      * @return:
      */
    public CommonResp delete(Long id) {

        CommonResp commonResp = new CommonResp();
        int flag = 0;
        flag = categoryMapper.deleteByPrimaryKey(id);
        if(flag == 0){
            commonResp.setSuccess(false);
        }
        return commonResp;
    }
}
