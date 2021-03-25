package com.example.pullpush.quartz.config;

import com.example.pullpush.enums.JobType;
import com.example.pullpush.properties.QuartzProperties;
import com.example.pullpush.quartz.job.SyncPullArticleOfCustomJob;
import com.example.pullpush.quartz.job.SyncPullArticleOfGatherJob;
import com.google.common.base.Objects;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class QuartzConfig {

    @Resource(name = "quartzProperties")
    private QuartzProperties quartzProperties;

    @Bean(name = "syncPullArticleOfCustomJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPullArticleOfCustomJobDetail(SyncPullArticleOfCustomJob syncPullArticleOfCustomJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(syncPullArticleOfCustomJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("execute");
        return jobDetail;
    }

    @Bean(name = "syncPullArticleOfCustomTrigger")
    public CronTriggerFactoryBean syncPullArticleOfCustomTrigger(JobDetail syncPullArticleOfCustomJobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(syncPullArticleOfCustomJobDetail);
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }

    @Bean(name = "syncPullArticleOfGatherJobDetail")
    public MethodInvokingJobDetailFactoryBean syncPullArticleOfGatherJobDetail(SyncPullArticleOfGatherJob syncPullArticleOfGatherJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(syncPullArticleOfGatherJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("execute");
        return jobDetail;
    }

    @Bean(name = "syncPullArticleOfGatherTrigger")
    public CronTriggerFactoryBean syncPullArticleOfGatherTrigger(JobDetail syncPullArticleOfGatherJobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(syncPullArticleOfGatherJobDetail);
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }

    @Bean(name = "schedulerFactory")
    public SchedulerFactoryBean schedulerFactory(Trigger syncPullArticleOfCustomTrigger, Trigger syncPullArticleOfGatherTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        // 延时启动，应用启动1秒后
        bean.setStartupDelay(1);
        // 注册触发器
        if (Objects.equal(quartzProperties.getJobType(), JobType.GATHER)) {
            bean.setTriggers(syncPullArticleOfGatherTrigger);
        }else{
            bean.setTriggers(syncPullArticleOfCustomTrigger);
        }
        return bean;
    }

}
