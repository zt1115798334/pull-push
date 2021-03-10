package com.example.pullpush.quartz.config;

import com.example.pullpush.quartz.listener.SimpleJobListener;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/10/12 10:28
 * description:
 */
@Configuration
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer {

    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        schedulerFactoryBean.setStartupDelay(2);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setGlobalJobListeners(new SimpleJobListener());
    }
}
