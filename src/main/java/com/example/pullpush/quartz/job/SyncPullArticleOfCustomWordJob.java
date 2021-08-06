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
public class SyncPullArticleOfCustomWordJob {
    @Resource(name = "customWordsByDateRange")
    private SyncPullArticleHandler.CustomWordsByDateRange customWordsByDateRange;

    @Resource(name = "customWordsByTimeRange")
    private SyncPullArticleHandler.CustomWordsByTimeRange customWordsByTimeRange;

    @Resource
    private QuartzProperties quartzProperties;

    public void execute() {
        log.info("执行 SyncPullArticleOfCustomWordJob");
        TimeType timeType = quartzProperties.getTimeType();
        Integer timeRange = quartzProperties.getTimeRange();
        long count;
        DateUtils.DateRange dateRange = DateUtils.intervalTimeCoverTimeType(timeType, timeRange);
        if (Objects.equal(timeType, TimeType.YEAR) ||
                Objects.equal(timeType, TimeType.MONTH) ||
                Objects.equal(timeType, TimeType.DAY)) {
            JSONObject extraParams = new JSONObject();
            extraParams.put("storageMode", StorageMode.INTERFACE);
            extraParams.put("startDate", dateRange.getStartDate());
            extraParams.put("endDate", dateRange.getEndDate());
            count = customWordsByDateRange.handlerData(extraParams);
        } else {
            JSONObject extraParams = new JSONObject();
            extraParams.put("storageMode", StorageMode.INTERFACE);
            extraParams.put("startDateTime", dateRange.getStartDateTime());
            extraParams.put("endDateTime", dateRange.getEndDateTime());
            extraParams.put("fromType", DateUtils.formatDate(LocalDate.now()));
            count = customWordsByTimeRange.handlerData(extraParams);
        }
        log.info("处理数量为：{}", count);
    }

}
