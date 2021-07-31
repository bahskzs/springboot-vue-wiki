//package com.yqy.wiki.job;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.time.DateUtils;
//import org.jetbrains.annotations.Async;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
///**
// * @author bahsk
// * @createTime 2021-07-30 11:42
// * @description
// */
//
//@Component
//public class TestJob {
//
//    private static final Logger LOG = LoggerFactory.getLogger(TestJob.class);
//
//    /*
//    *  固定时间间隔 fixedRate 单位毫秒
//    * */
//    @Scheduled(fixedRate = 1000)
//    public void simple() throws InterruptedException {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//        String time = simpleDateFormat.format(new Date());
//        Thread.sleep(2000);
//        LOG.info("time, {}",time);
//    }
//
//    /*
//    * 自定义cron表达式跑批处理
//    * 只有等上一批执行完，下一次才会在下一个时间跑完，错过就错过
//    * */
//    @Scheduled(cron = "*/2 * * * * ?")
//    public void cron() throws InterruptedException {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//        String time = simpleDateFormat.format(new Date());
//        Thread.sleep(2000);
//        LOG.info("time, {}",time);
//    }
//}
