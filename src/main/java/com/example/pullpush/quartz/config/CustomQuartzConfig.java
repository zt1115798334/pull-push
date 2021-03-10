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

@Configuration
public class CustomQuartzConfig {

	@Resource(name = "quartzProperties")
	private QuartzProperties quartzProperties;


}
