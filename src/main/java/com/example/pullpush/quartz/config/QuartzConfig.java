package com.example.pullpush.quartz.config;

import com.example.pullpush.properties.QuartzProperties;
import com.example.pullpush.quartz.job.SyncPullArticleOfCustomJob;
import com.example.pullpush.quartz.job.SyncPullArticleOfGatherJob;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class QuartzConfig {

    @Resource(name = "quartzProperties")
    private QuartzProperties quartzProperties;

    @Bean
    public JobDetailFactoryBean syncPullArticleOfCustomJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        // durability 表示任务完成之后是否依然保留到数据库，默认false
        jobDetailFactoryBean.setDurability(true);
        //当Quartz服务被中止后，再次启动或集群中其他机器接手任务时会尝试恢复执行之前未完成的所有任务
        jobDetailFactoryBean.setRequestsRecovery(true);
        jobDetailFactoryBean.setJobClass(SyncPullArticleOfCustomJob.class);
        jobDetailFactoryBean.setDescription("自定义词拉取数据定时器");
        Map<String, String> jobDataAsMap = new HashMap<>();
        jobDataAsMap.put("targetObject", "printTimeQuartz"); //spring 中bean的名字
        jobDataAsMap.put("targetMethod", "execute");   //执行方法名
        jobDetailFactoryBean.setJobDataAsMap(jobDataAsMap);
        return jobDetailFactoryBean;
    }

    @Bean
    public CronTriggerFactoryBean syncPullArticleOfCustomTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(Objects.requireNonNull(syncPullArticleOfCustomJobDetail().getObject()));
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }

    @Bean
    public JobDetailFactoryBean syncPullArticleOfGatherJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        // durability 表示任务完成之后是否依然保留到数据库，默认false
        jobDetailFactoryBean.setDurability(true);
        //当Quartz服务被中止后，再次启动或集群中其他机器接手任务时会尝试恢复执行之前未完成的所有任务
        jobDetailFactoryBean.setRequestsRecovery(true);
        jobDetailFactoryBean.setJobClass(SyncPullArticleOfGatherJob.class);
        jobDetailFactoryBean.setDescription("词库拉取数据定时器");
        Map<String, String> jobDataAsMap = new HashMap<>();
        jobDataAsMap.put("targetObject", "printTimeQuartz"); //spring 中bean的名字
        jobDataAsMap.put("targetMethod", "execute");   //执行方法名
        jobDetailFactoryBean.setJobDataAsMap(jobDataAsMap);
        return jobDetailFactoryBean;
    }

    @Bean
    public CronTriggerFactoryBean syncPullArticleOfGatherTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        // 设置jobDetail
        cronTriggerFactoryBean.setJobDetail(Objects.requireNonNull(syncPullArticleOfGatherJobDetail().getObject()));
        //秒 分 小时 日 月 星期 年  每10分钟
        cronTriggerFactoryBean.setCronExpression(quartzProperties.getCorn());//"0 0/30 * * * ?"
        //trigger超时处理策略 默认1：总是会执行头一次 2:不处理
        cronTriggerFactoryBean.setMisfireInstruction(2);
        return cronTriggerFactoryBean;
    }

}
