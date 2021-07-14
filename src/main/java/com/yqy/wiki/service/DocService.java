package com.yqy.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqy.wiki.domain.Content;
import com.yqy.wiki.domain.Doc;
import com.yqy.wiki.domain.DocExample;
import com.yqy.wiki.mapper.ContentMapper;
import com.yqy.wiki.mapper.DocMapper;
import com.yqy.wiki.req.DocQueryReq;
import com.yqy.wiki.req.DocSaveReq;
import com.yqy.wiki.resp.DocQueryResp;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.util.CopyUtil;
import com.yqy.wiki.util.SnowFlake;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bahsk
 * @createTime 2021-07-04 16:47
 * @description
 */

@Service
public class DocService {

    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * @author: bahsk
     * @date: 2021/6/18 21:47
     * @description: 返回输入模糊匹配电子书列表
     * @params: String name
     * @return: List<Doc>
     */
    public PageResp<DocQueryResp> list(DocQueryReq docQueryReq) {
        DocExample docExample = new DocExample();
        List<Doc> docList = new ArrayList<>();

        //类似一个where子句

        PageHelper.startPage(docQueryReq.getPage(), docQueryReq.getSize());
        docList = docMapper.selectByExample(null);

        //传统写法 ： 将List<Doc> 变为 List<DocReq> 需要用到循环 --> CopyUtil
//        List<DocReq> docReqList = new ArrayList<>();
//        for(Doc doc : docList) {
//            DocReq vo = new DocReq();
//            /* 传统写法 --> Spring提供了一个工具类 BeanUtils
//            vo.setId(doc.getId());
//            vo.setName(doc.getName());
//             */
//
//            BeanUtils.copyProperties(doc,vo);
//
//            docReqList.add(vo);
//        }
        // 采用可以复制List的工具类优化
        PageResp<DocQueryResp> docReqPageResp = new PageResp<>();
        PageInfo<DocQueryResp> docReqPageInfo = new PageInfo<>((List) docList);

        docReqPageResp.setTotal(docReqPageInfo.getTotal());
        docReqPageResp.setList(CopyUtil.copyList(docList, DocQueryResp.class));
        return docReqPageResp;
    }

    /**
     * @author: bahsk
     * @date: 2021/6/18 21:47
     * @description: 返回电子书列表
     * @params:
     * @return:
     */
    //TODO 需要改造下返回的内容,要包含Content相关的内容
    public List<DocQueryResp> all(Long ebookId) {

        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);

        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        // 列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }


    /**
     * @author: bahsk
     * @date: 2021/6/29 22:04
     * @description: 保存
     * @params:
     * @return:
     */
    //TODO 研究一下CopyUtil是怎么指定字段拷贝的 DONE 已经了解还是调用spring自带的方法
    public CommonResp save(DocSaveReq docSaveReq) {
        Doc doc = CopyUtil.copy(docSaveReq, Doc.class);
        Content content = CopyUtil.copy(docSaveReq, Content.class);
        CommonResp commonResp = new CommonResp();
        int flag = 0;

        if (ObjectUtils.isEmpty(doc.getId())) {
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);

        } else {
            docMapper.updateByPrimaryKey(doc);
            /*
             * doc有值,不代表content的有值,所以需要判断是否存在,
             * 但是也有可能点击保存方法并不是真的有改动内容，所以需要去掉flag这个值
             * */
            Content content1 = contentMapper.selectByPrimaryKey(content.getId());
            if (ObjectUtils.isEmpty(content1)) {
                content.setId(doc.getId());
                contentMapper.insert(content);
            } else {
                contentMapper.updateByPrimaryKeyWithBLOBs(content);
            }
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
        flag = docMapper.deleteByPrimaryKey(id);
        if (flag == 0) {
            commonResp.setSuccess(false);
        }
        return commonResp;
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        return content.getContent();
    }
}
