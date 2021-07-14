package com.yqy.wiki.mapper;


import com.yqy.wiki.domain.Content;
import com.yqy.wiki.domain.Doc;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author bahsk
 * @createTime 2021-07-09 15:06
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentMapperTest {

    @Resource
    ContentMapper contentMapper;

    @Resource
    DocMapper docMapper;

    @Test
    public void saveContent() {
        Content content = new Content();
        content.setId(111111L);
        content.setContent("aaa");
        int flag = 0;
        Assert.assertEquals(1, contentMapper.insert(content));

    }

    @Test
    public void saveDoc() {
        Doc doc = new Doc();
        doc.setId(111111L);
        doc.setEbookId(111111L);
        doc.setParent(0L);
        doc.setName("a");
        int flag = 0;
        Assert.assertEquals(1, docMapper.insert(doc));

    }

    @Test
    public void updateDoc() {
        Doc doc = new Doc();
        doc.setId(111111L);
        doc.setEbookId(111111L);
        doc.setParent(0L);
        doc.setName("aaa");
        int flag = 0;
        Assert.assertEquals(1, docMapper.updateByPrimaryKey(doc));

    }


    @Test
    public void updateContent() {
        Content content = new Content();
        content.setId(111111L);
        content.setContent("aaaaaaa");
        int flag = 0;
        Assert.assertEquals(1, contentMapper.updateByPrimaryKeyWithBLOBs(content));

    }
}
