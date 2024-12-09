package com.snowy.thinkbox.config;

import com.snowy.thinkbox.job.EbookUpdateJob;
import com.snowy.thinkbox.job.SnapshotUpdateJob;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@EnableScheduling
public class QuartzConfig {

    private final ApplicationContext applicationContext;

    // Constructor injection of the Spring ApplicationContext
    public QuartzConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    // JobDetail的通用方法
    private JobDetail jobDetail(Class<? extends Job> jobClass,String jobName) {
//        return JobBuilder.newJob(EbookUpdateJob.class)
//                .withIdentity("ebookUpdateJob")
//                .storeDurably()
//                .build();
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName)
                .storeDurably()
                .build();
    }

    // Trigger的通用方法（使用cron表达式的Trigger）
    private Trigger trigger(JobDetail jobDetail,String triggerName,String cronExpression) {
//        return TriggerBuilder.newTrigger()
//                .forJob(jobDetail)
//                .withIdentity("ebookUpdateTrigger")
//                .withSchedule(CronScheduleBuilder.cronSchedule("* 10 * * * ?")) // Every 30 seconds
//                .build();
                return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(triggerName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)) // Every 30 seconds
                .build();

    }

    // Create a SpringBeanJobFactory for Quartz to allow Spring to inject dependencies into the job
    @Bean
    public JobFactory jobFactory() {
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);  // Use Spring context for job creation
        return jobFactory;
    }

    // Configure the Scheduler with the custom JobFactory
    @Bean
    public Scheduler scheduler(JobFactory jobFactory) throws SchedulerException {
        // Create a scheduler
//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.setJobFactory(jobFactory);  // Set the custom job factory to inject dependencies
//        scheduler.scheduleJob(jobDetail, trigger);  // Schedule the job with the trigger
//        scheduler.start();  // Start the scheduler
//        return scheduler;
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.setJobFactory(jobFactory);

        //创建JobDetail实例，绑定Job实现类
        JobDetail ebookUpdateJobDetail = jobDetail(EbookUpdateJob.class,"ebookUpdateJob");
        JobDetail SnapshotUpdateJobDetail = jobDetail(SnapshotUpdateJob.class,"SnapshotUpdateJob");

        //创建触发器实例
        Trigger ebookUpdateJobTrigger = trigger(ebookUpdateJobDetail,"ebookUpdateTrigger","* 10 * * * ?");
        Trigger SnapshotUpdateJobTrigger = trigger(SnapshotUpdateJobDetail,"SnapshotUpdateTrigger","5 * * * * ?");

        //利用scheduler进行任务调度
        scheduler.scheduleJob(ebookUpdateJobDetail,ebookUpdateJobTrigger);
        scheduler.scheduleJob(SnapshotUpdateJobDetail,SnapshotUpdateJobTrigger);

        //启动
        scheduler.start();
        return scheduler;
    }
}
