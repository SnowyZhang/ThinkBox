package com.snowy.thinkbox.config;

import com.snowy.thinkbox.job.EbookUpdateJob;
import org.quartz.*;
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

    // Define the JobDetail for the EbookUpdateJob
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(EbookUpdateJob.class)
                .withIdentity("ebookUpdateJob")
                .storeDurably()
                .build();
    }

    // Define the Trigger with Cron Expression to schedule the job
    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("ebookUpdateTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("* 10 * * * ?")) // Every 30 seconds
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
    public Scheduler scheduler(JobDetail jobDetail, Trigger trigger, JobFactory jobFactory) throws SchedulerException {
        // Create a scheduler
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.setJobFactory(jobFactory);  // Set the custom job factory to inject dependencies
        scheduler.scheduleJob(jobDetail, trigger);  // Schedule the job with the trigger
        scheduler.start();  // Start the scheduler
        return scheduler;
    }
}
