package com.example.pullpush.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.base.controller.BaseResultMessage;
import com.example.pullpush.base.controller.ResultMessage;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.handler.SyncPullArticleHandler;
import com.example.pullpush.utils.DateUtils;
import com.example.pullpush.utils.MStringUtils;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource(name = "queryWordsByDateRange")
    private final SyncPullArticleHandler.QueryWordsByDateRange queryWordsByDateRange;

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

    @PostMapping("pullArticleOfQueryWords")
    public ResultMessage pullArticleOfQueryWords(@DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                 @RequestParam LocalDate startDate,
                                                 @DateTimeFormat(pattern = DateUtils.DATE_FORMAT)
                                                 @RequestParam LocalDate endDate,
                                                 @RequestParam StorageMode storageMode,
                                                 @RequestParam String queryWords) {
        JSONObject extraParams = new JSONObject();
        extraParams.put("storageMode", storageMode);
        extraParams.put("startDate", startDate);
        extraParams.put("endDate", endDate);
        extraParams.put("queryWords", MStringUtils.toDBC(queryWords).split(","));
        long handlerData = queryWordsByDateRange.handlerData(extraParams);
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


    public static void main(String[] args) {
        System.out.println(MStringUtils.toDBC("两会*（教育经费+教师工资+教育支出）,代表*（教育经费+教师工资+教育支出）,委员*（教育经费+教师工资+教育支出）"));
    }
}
