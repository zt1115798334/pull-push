package com.example.pullpush.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.base.handler.page.PageHandler;
import com.example.pullpush.dto.GatherWordDto;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.mysql.service.GatherWordsService;
import com.example.pullpush.properties.CustomWordProperties;
import com.example.pullpush.service.PullService;
import com.example.pullpush.utils.MStringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fan on 7/29/20.
 */
@Slf4j
@Component
@AllArgsConstructor
public class SyncPullArticleHandler {

    @Component("customWordsByDateRange")
    @AllArgsConstructor
    public static class CustomWordsByDateRange {

        private final PullService pullEsArticle;
        private final CustomWordProperties customWordProperties;

        public long handlerData(JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            List<String> gatherWords = customWordProperties.getWord().stream().map(MStringUtils::splitMinGranularityStr)
                    .flatMap(Collection::stream)
                    .distinct().collect(Collectors.toList());
            return pullEsArticle.pullEsArticleByDateRange(storageMode, gatherWords, startDate, endDate, "custom");
        }
    }


    @Component("gatherWordsByDateRange")
    @AllArgsConstructor
    public static class GatherWordsByDateRange extends PageHandler<GatherWordDto> {

        private final PullService pullEsArticle;

        private final GatherWordsService gatherWordsService;

        @Override
        protected long handleDataOfPerPage(List<GatherWordDto> list, int pageNumber, JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            List<String> gatherWords = list.stream().map(GatherWordDto::getName).collect(Collectors.toList());
            return pullEsArticle.pullEsArticleByDateRange(storageMode, gatherWords, startDate, endDate, "gather");
        }

        @Override
        protected Page<GatherWordDto> getPageList(int pageNumber, JSONObject extraParams) {
            if (extraParams.getBoolean("status")) {
                return gatherWordsService.findPageByEntityStatus(1L, pageNumber, DEFAULT_BATCH_SIZE);
            } else {
                return gatherWordsService.findPageByEntity(pageNumber, DEFAULT_BATCH_SIZE);
            }
        }
    }

    @Component("customWordsByTimeRange")
    @AllArgsConstructor
    public static class CustomWordsByTimeRange {

        private final PullService pullEsArticle;

        private final CustomWordProperties customWordProperties;

        public long handlerData(JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDateTime startDateTime = extraParams.getObject("startDateTime", LocalDateTime.class);
            LocalDateTime endDateTime = extraParams.getObject("endDateTime", LocalDateTime.class);
            String fromType = extraParams.getString("fromType");
            List<String> gatherWords = customWordProperties.getWord().stream().map(MStringUtils::splitMinGranularityStr)
                    .flatMap(Collection::stream)
                    .distinct().collect(Collectors.toList());
            return pullEsArticle.pullEsArticleByTimeRange(storageMode, gatherWords, startDateTime, endDateTime, fromType);
        }
    }

    @Component("gatherWordsByTimeRange")
    @AllArgsConstructor
    public static class GatherWordsByTimeRange extends PageHandler<GatherWordDto> {

        private final PullService pullEsArticle;

        private final GatherWordsService gatherWordsService;

        @Override
        protected long handleDataOfPerPage(List<GatherWordDto> list, int pageNumber, JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            log.info("GatherWordDto:{}", list);
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDateTime startDateTime = extraParams.getObject("startDateTime", LocalDateTime.class);
            LocalDateTime endDateTime = extraParams.getObject("endDateTime", LocalDateTime.class);
            String fromType = extraParams.getString("fromType");
            List<String> gatherWords = list.stream().map(GatherWordDto::getName).collect(Collectors.toList());
            return pullEsArticle.pullEsArticleByTimeRange(storageMode, gatherWords, startDateTime, endDateTime, fromType);
        }

        @Override
        protected Page<GatherWordDto> getPageList(int pageNumber, JSONObject extraParams) {
            if (extraParams.getBoolean("status")) {
                return gatherWordsService.findPageByEntityStatus(1L, pageNumber, DEFAULT_BATCH_SIZE);
            } else {
                return gatherWordsService.findPageByEntity(pageNumber, DEFAULT_BATCH_SIZE);
            }
        }
    }
}
