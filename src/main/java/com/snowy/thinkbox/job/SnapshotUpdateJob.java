package com.snowy.thinkbox.job;

import com.snowy.thinkbox.controller.ControllerExceptionHandler;
import com.snowy.thinkbox.service.DocService;
import com.snowy.thinkbox.service.EbookSnapshotService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@DisallowConcurrentExecution
public class SnapshotUpdateJob implements Job {
    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //这是不开启一次进行多个数据库操作的情况
//        long start = System.currentTimeMillis();
//        log.info("SnapshotUpdateJob start：{}", start);
//        ebookSnapshotService.insertEbookSnapshotItem();
//        ebookSnapshotService.updateCount();
//        ebookSnapshotService.updateIncrease();
//        long end = System.currentTimeMillis();
//        log.info("SnapshotUpdateJob end：{}", end);
        //这是经过数据库连接的多查询参数配置之后，开启一次进行多个数据库操作的情况
        long start = System.currentTimeMillis();
        log.info("SnapshotUpdateJob start：{}", start);
        ebookSnapshotService.SnapshotGenerate();
        long end = System.currentTimeMillis();
        log.info("SnapshotUpdateJob end：{}", end);
    }
}

