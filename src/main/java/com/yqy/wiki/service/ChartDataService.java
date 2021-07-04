package com.yqy.wiki.service;

import com.yqy.wiki.domain.ChartData;
import com.yqy.wiki.mapper.ChartDataMapper;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.util.CopyUtil;
import com.yqy.wiki.req.ChartDataReq;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author bahsk
 * @createTime 2021-06-25 12:44
 * @description
 */

@Service
public class ChartDataService {

    @Resource
    private ChartDataMapper chartDataMapper;

    /* @Author: cat
     * @Description: 返回查询t_chart的业务方法
     * @Date: 12:49 2021-06-25
     []
     * @MethodName: query
     * @return: com.yqy.wiki.resp.CommonResp<java.util.List<com.yqy.wiki.vo.ChartDataVO>>
     * @Version: 1.0
     */
    public CommonResp<List<ChartDataReq>> query() {

        CommonResp<List<ChartDataReq>> listCommonResp = new CommonResp<List<ChartDataReq>>();
        List<ChartData> chartDataList = chartDataMapper.selectByExample(null);
        List<ChartDataReq> chartDataReqList = CopyUtil.copyList(chartDataList, ChartDataReq.class);
        listCommonResp.setContent(chartDataReqList);
        return listCommonResp;
    }

}
