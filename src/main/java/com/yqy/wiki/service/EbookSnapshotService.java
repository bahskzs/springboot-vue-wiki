package com.yqy.wiki.service;

import com.yqy.wiki.mapper.EbookSnapshotMapper;
import com.yqy.wiki.mapper.EbookSnapshotMapperCustom;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author bahsk
 * @createTime 2021-08-01 11:23
 * @description
 */
@Service
public class EbookSnapshotService {

    @Resource
    private EbookSnapshotMapperCustom ebookSnapshotMapperCustom;

    /*
     * 快照
     * */
    public void genSnapshot() {
        ebookSnapshotMapperCustom.genEbookSnapshot();
    }
}
