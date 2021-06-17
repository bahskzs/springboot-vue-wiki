package com.yqy.wiki.service;

import com.yqy.wiki.domain.Ebook;
import com.yqy.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }

}
