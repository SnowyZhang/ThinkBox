package com.snowy.thinkbox.job;

import com.snowy.thinkbox.controller.ControllerExceptionHandler;
import com.snowy.thinkbox.service.DocService;
import com.snowy.thinkbox.service.EbookService;
import jakarta.annotation.Resource;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@DisallowConcurrentExecution
public class EbookUpdateJob implements Job {
    @Autowired
    private DocService docService;

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //开启时间记录
        long start = System.currentTimeMillis();
        log.info("EbookUpdateJob start：{}", start);
        docService.updateEbookInfo();
        long end = System.currentTimeMillis();
        log.info("EbookUpdateJob end：{}", end);
    }
}

