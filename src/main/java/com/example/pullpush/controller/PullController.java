package com.example.pullpush.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.base.controller.BaseResultMessage;
import com.example.pullpush.base.controller.ResultMessage;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.handler.SyncPullArticleHandler;
import com.example.pullpush.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

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

    @Resource(name = "customWordsByDay")
    private final SyncPullArticleHandler.CustomWordsByDay customWordsByDay;

    @Resource(name = "gatherWordsByDay")
    private final SyncPullArticleHandler.GatherWordsByDay gatherWordsByDay;

    @GetMapping("pullArticleOfCustomWords")
    public ResultMessage pullArticleOfCustomWords(@DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate startDate,
                                                  @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                  @RequestParam LocalDate endDate) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", StorageMode.LOCAL);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        long handlerData = customWordsByDay.handlerData(extraParams);
        return success(handlerData);
    }

    @GetMapping("pullArticleOfGatherWords")
    public ResultMessage pullArticleOfGatherWords(@RequestParam String startDate, @RequestParam String endDate, @RequestParam Boolean status) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", StorageMode.INTERFACE);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        extraParams.put("status", status);
        gatherWordsByDay.handle(extraParams);
        return success();
    }


}
