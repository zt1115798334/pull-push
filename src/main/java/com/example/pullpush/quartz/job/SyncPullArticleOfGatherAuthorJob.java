package com.example.pullpush.quartz.job;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.enums.TimeType;
import com.example.pullpush.handler.SyncPullArticleHandler;
import com.example.pullpush.properties.QuartzProperties;
import com.example.pullpush.utils.DateUtils;
import com.google.common.base.Objects;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;

@Component
@EnableScheduling
public class SyncPullArticleOfGatherAuthorJob {

    @Resource(name = "gatherAuthorsByDateRange")
    private SyncPullArticleHandler.GatherAuthorsByDateRange gatherAuthorsByDateRange;

    @Resource(name = "gatherAuthorsByTimeRange")
    private SyncPullArticleHandler.GatherAuthorsByTimeRange gatherAuthorsByTimeRange;

    @Resource
    private QuartzProperties quartzProperties;

    public void execute() {
        TimeType timeType = quartzProperties.getTimeType();
        Integer timeRange = quartzProperties.getTimeRange();
        DateUtils.DateRange dateRange = DateUtils.intervalTimeCoverTimeType(timeType, timeRange);
        if (Objects.equal(timeType, TimeType.YEAR) ||
                Objects.equal(timeType, TimeType.MONTH) ||
                Objects.equal(timeType, TimeType.DAY)) {
            JSONObject extraParams = new JSONObject();
            extraParams.put("storageMode", StorageMode.INTERFACE);
            extraParams.put("startDate", dateRange.getStartDate());
            extraParams.put("endDate", dateRange.getEndDate());
            gatherAuthorsByDateRange.handle(extraParams);
        } else {
            JSONObject extraParams = new JSONObject();
            extraParams.put("storageMode", StorageMode.INTERFACE);
            extraParams.put("startDateTime", dateRange.getStartDateTime());
            extraParams.put("endDateTime", dateRange.getEndDateTime());
            extraParams.put("status", true);
            extraParams.put("fromType", DateUtils.formatDate(LocalDate.now()));
            gatherAuthorsByTimeRange.handle(extraParams);
        }

    }
}
