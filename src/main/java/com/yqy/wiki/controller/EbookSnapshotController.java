package com.yqy.wiki.controller;

import com.yqy.wiki.req.EbookQueryReq;
import com.yqy.wiki.req.EbookSaveReq;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.EbookQueryResp;
import com.yqy.wiki.resp.EbookStatisticResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.service.EBookService;
import com.yqy.wiki.service.EbookSnapshotService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author bahsk
 * @createTime 2021-06-18 0:36
 * @description
 */

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/get-statistic")
    public CommonResp getStatistic() {
        List<EbookStatisticResp> statisticRespList = ebookSnapshotService.getStatistic();
        CommonResp<List<EbookStatisticResp>> commonResp = new CommonResp<>();
        commonResp.setContent(statisticRespList);
        return commonResp;
    }

    @GetMapping("/get-30-statistic")
    public CommonResp get30Statistic() {
        List<EbookStatisticResp> statisticRespList = ebookSnapshotService.get30Statistic();
        CommonResp<List<EbookStatisticResp>> commonResp = new CommonResp<>();
        commonResp.setContent(statisticRespList);
        return commonResp;
    }


}
