package com.kfit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
//@Configuration
//@EnableScheduling
public class TaskService {

//    @Bean
//    public ScheduledExecutorService scheduledExecutorService() {
//        ScheduledExecutorService ss = Executors.newScheduledThreadPool(20);
//        return ss;
//    }
//
//    @Bean
//    public TaskScheduler taskScheduler() {
//        return new ConcurrentTaskScheduler();
//    }

//    @Bean
//    public TaskScheduler poolScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setThreadNamePrefix("poolScheduler");
//        scheduler.setPoolSize(10);
//        return scheduler;
//    }

    @Scheduled(cron = "0/2 * * * * ?")//每5秒执行一次
    //@Scheduled(fixedRate = 1000*60*1)
    public void syncCameraRunStatus() {
        System.out.println("youqiang");
    }
}
