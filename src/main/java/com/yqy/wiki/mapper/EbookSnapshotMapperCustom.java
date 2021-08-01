package com.yqy.wiki.mapper;

import com.yqy.wiki.domain.EbookSnapshot;
import com.yqy.wiki.domain.EbookSnapshotExample;
import com.yqy.wiki.resp.EbookStatisticResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EbookSnapshotMapperCustom {

   public void genEbookSnapshot();

   List<EbookStatisticResp> getStatistic();

   List<EbookStatisticResp> get30Statistic();


}
