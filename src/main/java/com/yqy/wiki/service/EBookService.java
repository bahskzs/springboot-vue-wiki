package com.yqy.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqy.wiki.domain.Ebook;
import com.yqy.wiki.domain.EbookExample;
import com.yqy.wiki.mapper.EbookMapper;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.EbookQueryResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.util.CopyUtil;
import com.yqy.wiki.util.SnowFlake;
import com.yqy.wiki.vo.EbookQueryVO;
import com.yqy.wiki.vo.EbookSaveVO;
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
public class EBookService {

    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

     /**
      * @author: bahsk
      * @date: 2021/6/18 21:47
      * @description: 返回输入模糊匹配电子书列表
      * @params: String name
      * @return: List<Ebook>
      */
    public PageResp<EbookQueryResp> list(EbookQueryVO ebookQueryVO) {
        EbookExample ebookExample = new EbookExample();
        List<Ebook> ebookList = new ArrayList<>();

        //类似一个where子句
        if(StringUtils.hasLength(ebookQueryVO.getName())) {
            EbookExample.Criteria criteria = ebookExample.createCriteria();
            criteria.andNameLike("%" + ebookQueryVO.getName() + "%");
            PageHelper.startPage(ebookQueryVO.getPage(), ebookQueryVO.getSize());
            ebookList = ebookMapper.selectByExample(ebookExample);
        }else{
            PageHelper.startPage(ebookQueryVO.getPage(), ebookQueryVO.getSize());
            ebookList = ebookMapper.selectByExample(null);
        }
        //传统写法 ： 将List<Ebook> 变为 List<EbookVO> 需要用到循环 --> CopyUtil
//        List<EbookVO> ebookVOList = new ArrayList<>();
//        for(Ebook ebook : ebookList) {
//            EbookVO vo = new EbookVO();
//            /* 传统写法 --> Spring提供了一个工具类 BeanUtils
//            vo.setId(ebook.getId());
//            vo.setName(ebook.getName());
//             */
//
//            BeanUtils.copyProperties(ebook,vo);
//
//            ebookVOList.add(vo);
//        }
        // 采用可以复制List的工具类优化
        PageResp<EbookQueryResp> ebookVOPageResp = new PageResp<>();
        PageInfo<EbookQueryResp> ebookVOPageInfo = new PageInfo<>((List)ebookList);

        ebookVOPageResp.setTotal(ebookVOPageInfo.getTotal());
        ebookVOPageResp.setList(CopyUtil.copyList(ebookList, EbookQueryResp.class));
        return ebookVOPageResp;
    }

     /**
      * @author: bahsk
      * @date: 2021/6/18 21:47
      * @description: 返回电子书列表
      * @params:
      * @return:
      */
    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }



     /**
      * @author: bahsk
      * @date: 2021/6/29 22:04
      * @description: 保存
      * @params:
      * @return:
      */
    public CommonResp save(EbookSaveVO ebookSaveVO) {
        Ebook ebook = CopyUtil.copy(ebookSaveVO, Ebook.class);
        CommonResp commonResp = new CommonResp();
        int flag = 0;
        if (ObjectUtils.isEmpty(ebook.getId())){
            ebook.setId(snowFlake.nextId());
            flag = ebookMapper.insert(ebook);
        }else{
            flag = ebookMapper.updateByPrimaryKey(ebook);
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
        flag = ebookMapper.deleteByPrimaryKey(id);
        if(flag == 0){
            commonResp.setSuccess(false);
        }
        return commonResp;
    }
}
