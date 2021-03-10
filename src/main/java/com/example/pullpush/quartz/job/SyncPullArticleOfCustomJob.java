package com.example.pullpush.quartz.job;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.handler.SyncPullArticleHandler;
import com.example.pullpush.utils.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;

@Component
public class SyncPullArticleOfCustomJob implements Job {
    @Resource(name = "customWordsByTimeRange")
    private SyncPullArticleHandler.CustomWordsByTimeRange customWordsByTimeRange;

    @Override
    public void execute(JobExecutionContext context) {
        LocalDate localDate = DateUtils.currentDate();
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", StorageMode.INTERFACE);
        extraParams.put("startDate", localDate.plusMonths(-1));
        extraParams.put("endDate", localDate);
        extraParams.put("fromType", DateUtils.formatDate(LocalDate.now()));
        long handlerData = customWordsByTimeRange.handlerData(extraParams);
    }

}
