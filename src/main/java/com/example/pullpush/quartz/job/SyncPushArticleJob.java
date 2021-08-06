package com.example.pullpush.quartz.job;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.enums.TimeType;
import com.example.pullpush.handler.SyncPullArticleHandler;
import com.example.pullpush.properties.QuartzProperties;
import com.example.pullpush.service.PushService;
import com.example.pullpush.utils.DateUtils;
import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@EnableScheduling
public class SyncPushArticleJob {

    @Resource
    private PushService pushService;

    public void execute() throws ExecutionException, InterruptedException {
        log.info("执行 SyncPushArticleJob");
        pushService.start();
    }
}
