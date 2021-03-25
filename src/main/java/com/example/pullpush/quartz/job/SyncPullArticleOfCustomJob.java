package com.example.pullpush.quartz.job;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.enums.TimeType;
import com.example.pullpush.handler.SyncPullArticleHandler;
import com.example.pullpush.properties.QuartzProperties;
import com.example.pullpush.utils.DateUtils;
import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@EnableScheduling
public class SyncPullArticleOfCustomJob {
    @Resource(name = "customWordsByDateRange")
    private SyncPullArticleHandler.CustomWordsByDateRange customWordsByDateRange;

    @Resource(name = "customWordsByTimeRange")
    private SyncPullArticleHandler.CustomWordsByTimeRange customWordsByTimeRange;

    @Resource
    private QuartzProperties quartzProperties;

    public void execute() {
        TimeType timeType = quartzProperties.getTimeType();
        Integer timeRange = quartzProperties.getTimeRange();
        long count;
        if (Objects.equal(timeType, TimeType.YEAR) ||
                Objects.equal(timeType, TimeType.MONTH) ||
                Objects.equal(timeType, TimeType.DAY)) {
            LocalDate localDate = DateUtils.currentDate();
            JSONObject extraParams = new JSONObject();
            LocalDate startDate = localDate;
            if (Objects.equal(timeType, TimeType.YEAR)) {
                startDate = localDate.plusYears(timeRange);
            } else if (Objects.equal(timeType, TimeType.MONTH)) {
                startDate = localDate.plusMonths(timeRange);
            } else if (Objects.equal(timeType, TimeType.DAY)) {
                startDate = localDate.plusDays(timeRange);
            }
            extraParams.put("storageMode", StorageMode.INTERFACE);
            extraParams.put("startDate", startDate);
            extraParams.put("endDate", localDate);
            count = customWordsByDateRange.handlerData(extraParams);
        } else {
            LocalDateTime localDateTime = DateUtils.currentDateTime();
            JSONObject extraParams = new JSONObject();
            LocalDateTime startDateTime = localDateTime;
            if (Objects.equal(timeType, TimeType.HOUR)) {
                startDateTime = localDateTime.plusHours(timeRange);
            } else if (Objects.equal(timeType, TimeType.MINUTE)) {
                startDateTime = localDateTime.plusMinutes(timeRange);
            } else if (Objects.equal(timeType, TimeType.SECOND)) {
                startDateTime = localDateTime.plusSeconds(timeRange);
            }
            extraParams.put("storageMode", StorageMode.INTERFACE);
            extraParams.put("startDateTime", startDateTime);
            extraParams.put("endDateTime", localDateTime);
            extraParams.put("fromType", DateUtils.formatDate(LocalDate.now()));
            count = customWordsByTimeRange.handlerData(extraParams);
        }
        log.info("处理数量为：{}", count);
    }

}
