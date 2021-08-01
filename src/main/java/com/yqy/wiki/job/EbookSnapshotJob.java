package com.yqy.wiki.job;

import com.yqy.wiki.service.DocService;
import com.yqy.wiki.service.EbookSnapshotService;
import com.yqy.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author bahsk
 * @createTime 2021-07-30 11:42
 * @description
 */

@Component
public class EbookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);

    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @Resource
    private SnowFlake snowFlake;

    /*
     * 每30s更新电子书信息
     * 只有等上一批执行完，下一次才会在下一个时间跑完，错过就错过
     * */
    @Scheduled(cron = "15/60 * * * * ?")
    public void cron() {

        //增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("快照电子书表");
        long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        LOG.info("快照电子书表数据结束,耗时: {}毫秒", System.currentTimeMillis()
                - start);
    }
}
