package com.yqy.wiki.controller;

import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.service.ChartDataService;
import com.yqy.wiki.req.ChartDataReq;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author bahsk
 * @createTime 2021-06-25 12:50
 * @description
 */

@RestController
@RequestMapping("/chart")
public class ChartDataController {

    @Resource
    private ChartDataService chartDataService;


    @GetMapping("/list")
    public CommonResp<List<ChartDataReq>> query() {
        return chartDataService.query();
    }
}
