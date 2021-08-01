package com.yqy.wiki.service;

import com.yqy.wiki.mapper.EbookSnapshotMapper;
import com.yqy.wiki.mapper.EbookSnapshotMapperCustom;
import com.yqy.wiki.resp.EbookStatisticResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    /*
     * 获取首页数值数据：总阅读数、总点赞数、今日阅读数、今日点赞数、今日预计阅读数、今日预计阅读增长
     * */
    public List<EbookStatisticResp> getStatistic() {
        return ebookSnapshotMapperCustom.getStatistic();
    }

    public List<EbookStatisticResp> get30Statistic() {
        return ebookSnapshotMapperCustom.get30Statistic();
    }
}
