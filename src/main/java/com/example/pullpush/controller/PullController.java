package com.example.pullpush.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.base.controller.BaseResultMessage;
import com.example.pullpush.base.controller.ResultMessage;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.handler.SyncPullArticleHandler;
import com.example.pullpush.quartz.job.SyncPullArticleOfCustomAuthorJob;
import com.example.pullpush.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 9:48
 * description: 应用中心
 */

@AllArgsConstructor
@RestController
@RequestMapping("api")
public class PullController extends BaseResultMessage {

    @Resource(name = "customWordsByDateRange")
    private final SyncPullArticleHandler.CustomWordsByDateRange customWordsByDateRange;

    @Resource(name = "gatherWordsByDateRange")
    private final SyncPullArticleHandler.GatherWordsByDateRange gatherWordsByDateRange;

    @Resource(name = "customAuthorsByDateRange")
    private final SyncPullArticleHandler.CustomAuthorsByDateRange customAuthorsByDateRange;

    @Resource(name = "gatherAuthorsByDateRange")
    private final SyncPullArticleHandler.GatherAuthorsByDateRange gatherAuthorsByDateRange;

    @GetMapping("pullArticleOfCustomWords")
    public ResultMessage pullArticleOfCustomWords(@DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate startDate,
                                                  @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate endDate,
                                                  @RequestParam StorageMode storageMode) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        long handlerData = customWordsByDateRange.handlerData(extraParams);
        return success(handlerData);
    }

    @GetMapping("pullArticleOfGatherWords")
    public ResultMessage pullArticleOfGatherWords(@DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate startDate,
                                                  @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate endDate,
                                                  @RequestParam StorageMode storageMode,
                                                  @RequestParam(defaultValue = "true") Boolean status) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        extraParams.put("status", status);
        gatherWordsByDateRange.handle(extraParams);
        return success();
    }

    @GetMapping("pullArticleOfCustomAuthors")
    public ResultMessage pullArticleOfCustomAuthors(@DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate startDate,
                                                  @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate endDate,
                                                  @RequestParam StorageMode storageMode) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        long handlerData = customAuthorsByDateRange.handlerData(extraParams);
        return success(handlerData);
    }

    @GetMapping("pullArticleOfGatherAuthors")
    public ResultMessage pullArticleOfGatherAuthors(@DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate startDate,
                                                  @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate endDate,
                                                  @RequestParam StorageMode storageMode) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        gatherAuthorsByDateRange.handle(extraParams);
        return success();
    }


}
