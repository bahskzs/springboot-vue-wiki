package com.yqy.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqy.wiki.domain.Content;
import com.yqy.wiki.domain.Doc;
import com.yqy.wiki.domain.DocExample;
import com.yqy.wiki.exception.BusinessException;
import com.yqy.wiki.exception.BusinessExceptionCode;
import com.yqy.wiki.mapper.ContentMapper;
import com.yqy.wiki.mapper.DocMapper;
import com.yqy.wiki.mapper.DocMapperCustom;
import com.yqy.wiki.req.DocQueryReq;
import com.yqy.wiki.req.DocSaveReq;
import com.yqy.wiki.resp.DocQueryResp;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.util.CopyUtil;
import com.yqy.wiki.util.RedisUtil;
import com.yqy.wiki.util.RequestContext;
import com.yqy.wiki.util.SnowFlake;
import com.yqy.wiki.websocket.WebSocketServer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private DocMapperCustom docMapperCustom;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    public RedisUtil redisUtil;

    @Resource
    private WebSocketServer webSocketServer;

    @Resource
    private WsService wsService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

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
    @Transactional
    public CommonResp save(DocSaveReq docSaveReq) {
        Doc doc = CopyUtil.copy(docSaveReq, Doc.class);
        Content content = CopyUtil.copy(docSaveReq, Content.class);
        CommonResp commonResp = new CommonResp();
        int flag = 0;

        if (ObjectUtils.isEmpty(doc.getId())) {
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
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
        //return content.getContent();  直接返回如果没有内容会报空指针异常
        //文档阅读数 + 1
        LOG.info("文档阅读数+1");
        docMapperCustom.increaseViewCountByPrimaryKey(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    /**
     * @author: bahsk
     * @date: 2021/7/29 16:02
     * @description: 点赞
     * @params:
     * @return:
     */
    public void vote(Long id) {
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3)) {
            docMapperCustom.increaseVoteCountByPrimaryKey(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }

        //推送点赞的消息
        // xxx文档被点赞
        Doc docDB = docMapper.selectByPrimaryKey(id);
        String logId = MDC.get("LOG_ID");
        wsService.sendInfo("【" + docDB.getName() + "】文档被点赞!", logId);
//        rocketMQTemplate.convertAndSend("VOTE_TOPIC","【" + docDB.getName() + "】文档被点赞!");

    }

    /*
     * 根据文档汇总点赞数，阅读数，文档数到电子书
     * */
    public void refreshEbookByDoc() {
        docMapperCustom.refreshEbookInfo();
    }
}
