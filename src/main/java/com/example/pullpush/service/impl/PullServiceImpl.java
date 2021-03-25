package com.example.pullpush.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.analysis.service.AnalysisService;
import com.example.pullpush.custom.CustomPage;
import com.example.pullpush.dto.FileInfoDto;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.es.domain.EsArticle;
import com.example.pullpush.es.service.EsArticleService;
import com.example.pullpush.properties.EsProperties;
import com.example.pullpush.service.PullService;
import com.example.pullpush.service.callable.SendInInterface;
import com.example.pullpush.utils.DateUtils;
import com.google.common.base.Objects;
import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PullServiceImpl implements PullService {

    private final EsArticleService esArticleService;

    private final EsProperties esProperties;

    private final AnalysisService analysisService;

    @Override
    public long pullEsArticleByDateRange(StorageMode storageMode, List<String> gatherWords,
                                         LocalDate startDate, LocalDate endDate,
                                         String fromType) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        JSONArray related = JSONArray.parseArray(JSONArray.toJSONString(gatherWords));
        List<LocalDate> localDates = DateUtils.dateRangeList(startDate, endDate);
        for (LocalDate localDate : localDates) {
            String mapKey = "day_" + fromType + "_" + DateUtils.formatDate(localDate);
            executorService.submit(new PullArticleHandle(storageMode, esArticleService, analysisService, related,
                    localDate.atTime(LocalTime.of(0, 0, 0)),
                    localDate.atTime(LocalTime.of(23, 59, 59)),
                    esProperties.getPageSize(), mapKey, esProperties.getFilePath()));
        }
        executorService.shutdown();
        return 0;
    }

    @Override
    public long pullEsArticleByTimeRange(StorageMode storageMode, List<String> gatherWords, LocalDateTime startDateTime, LocalDateTime endDateTime, String fromType) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        JSONArray related = JSONArray.parseArray(JSONArray.toJSONString(gatherWords));
        String mapKey = "timeRange_" + fromType;
        executorService.submit(new PullArticleHandle(storageMode, esArticleService, analysisService, related, startDateTime, endDateTime, esProperties.getPageSize(), mapKey, esProperties.getFilePath()));
        executorService.shutdown();
        return 0;
    }


    @AllArgsConstructor
    public static class PullArticleHandle implements Callable<Long> {

        private final StorageMode storageMode;

        private final EsArticleService esArticleService;

        private final AnalysisService analysisService;

        private final JSONArray related;

        private final LocalDateTime startDateTime;

        private final LocalDateTime endDateTime;

        private final int pageSize;

        private final String mapKey;

        private final String filePath;

        @Override
        public Long call() {

            ExecutorService executorService = Executors.newCachedThreadPool();

            ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>(1000);
            AtomicInteger atomicInteger = new AtomicInteger();

            while (true) {
                String scrollId = concurrentHashMap.getOrDefault(mapKey, StringUtils.EMPTY);
                CustomPage<EsArticle> allDataEsArticlePage = esArticleService.findAllDataEsArticlePage(related, scrollId,
                        startDateTime, endDateTime, pageSize);
                if (allDataEsArticlePage.getScrollId() != null) {
                    concurrentHashMap.put(mapKey, allDataEsArticlePage.getScrollId());
                }
                List<EsArticle> articleList = allDataEsArticlePage.getList();
                if (allDataEsArticlePage.getTotalElements() == 0 || articleList.size() == 0) {
                    break;
                }
                RateLimiter rateLimiter = RateLimiter.create(100);
                List<Future<Long>> futureList = articleList.stream().map(esArticle -> {
                    String ossPath = esArticle.getOssPath();
                    String fileName = ossPath.substring(ossPath.indexOf("_") + 1);
                    FileInfoDto fileInfoDto = FileInfoDto.builder().filename(fileName).content(getArticleJson(esArticle)).build();
                    Callable<Long> callable = Objects.equal(storageMode, StorageMode.LOCAL) ?
                            new WriteInLocal(startDateTime.toLocalDate(), getArticleJson(esArticle), filePath, fileName, atomicInteger)
                            : new SendInInterface(rateLimiter, analysisService, fileInfoDto);
                    return executorService.submit(callable);
                }).collect(Collectors.toList());
            }
            executorService.shutdown();
            return null;
        }
    }

    @Slf4j
    @AllArgsConstructor
    public static class WriteInLocal implements Callable<Long> {

        private final LocalDate localDate;

        private final String articleJson;

        private final String filePath;

        private final String fileName;

        private final AtomicInteger atomicInteger;

        @Override
        public Long call() {
            String formatDate = DateUtils.formatDate(localDate);

            String fileNum = String.valueOf(atomicInteger.getAndIncrement() % 10);
            String realPath = filePath + File.separator + formatDate + File.separator + fileNum + File.separator;
            File file = new File(realPath);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (mkdirs) {
                    log.info("路径：{},不存在，现在创建完成", realPath);
                }
            }
            Path path = Paths.get(realPath + File.separator + fileName);
            try (BufferedWriter writer =
                         Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write(articleJson);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public static String getArticleJson(EsArticle esArticle) {
        JSONObject params = new JSONObject();
        params.put("SpiderInfo", "军犬舆情平台数据推送");
        params.put("ConfigInfo", "");
        params.put("ColumnURL", "");
        params.put("RegularName", esArticle.getSiteName());
        params.put("ConfigTag", "");
        params.put("KeywordID", "");
        params.put("Keyword", "");
        params.put("KeywordTag", "");
        params.put("Country", esArticle.getRegion());
        params.put("Carrie", esArticle.getCarrier());
        params.put("ColumnName", esArticle.getColumnName());
        params.put("Profession", "1000");
        params.put("Area", "");
        params.put("GatherTime", DateUtils.formatDateTime(esArticle.getGatherTime()));
        params.put("OrgURL", esArticle.getUrl());
        params.put("URL", esArticle.getUrl());
        params.put("PublishTime", DateUtils.formatDateTime(esArticle.getPublishTime()));
        params.put("Author", esArticle.getAuthor());
        params.put("Content", esArticle.getContent());
        params.put("Title", esArticle.getTitle());
        params.put("SiteName", esArticle.getSiteName());
        return params.toJSONString();
    }

    public static void main(String[] args) {
        String x = "资助*& #40;学生+学校& #41;".replace(" ","");
        System.out.println(StringEscapeUtils.unescapeHtml4(x));
    }
}
