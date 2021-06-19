package com.yqy.wiki.service;

import com.yqy.wiki.domain.Ebook;
import com.yqy.wiki.domain.EbookExample;
import com.yqy.wiki.mapper.EbookMapper;
import com.yqy.wiki.util.CopyUtil;
import com.yqy.wiki.vo.EbookVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

     /**
      * @author: bahsk
      * @date: 2021/6/18 21:47
      * @description: 返回输入模糊匹配电子书列表
      * @params: String name
      * @return: List<Ebook>
      */
    public List<EbookVO> list(EbookVO ebookVO) {
        EbookExample ebookExample = new EbookExample();
        //类似一个where子句
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + ebookVO.getName() + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

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
        List<EbookVO> ebookVOList = CopyUtil.copyList(ebookList,EbookVO.class);
        return ebookVOList;
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

}
