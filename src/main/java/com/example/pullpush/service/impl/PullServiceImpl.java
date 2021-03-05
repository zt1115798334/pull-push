package com.example.pullpush.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.custom.CustomPage;
import com.example.pullpush.entity.GatherWordInfo;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.es.domain.EsArticle;
import com.example.pullpush.es.service.EsArticleService;
import com.example.pullpush.properties.EsProperties;
import com.example.pullpush.service.PullService;
import com.example.pullpush.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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

    @Override
    public long pullEsArticle(StorageMode storageMode, List<GatherWordInfo> gatherWordInfos,
                              LocalDate startDate, LocalDate endDate,
                              String fromType) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<String> gatherWordInfo = gatherWordInfos.stream().map(GatherWordInfo::getName).collect(Collectors.toList());
        JSONArray related = JSONArray.parseArray(JSONArray.toJSONString(gatherWordInfo));
        List<LocalDate> localDates = DateUtils.dateRangeList(startDate, endDate);
        for (LocalDate localDate : localDates) {
            executorService.submit(new PullArticleHandle(esArticleService, related, localDate, esProperties.getPageSize(), fromType, esProperties.getFilePath()));
        }
        executorService.shutdown();
        return 0;
    }


    @AllArgsConstructor
    public static class PullArticleHandle implements Callable<Long> {

        private final EsArticleService esArticleService;

        private final JSONArray related;

        private final LocalDate localDate;

        private final int pageSize;

        private final String fromType;

        private final String filePath;

        @Override
        public Long call() {

            ExecutorService executorService = Executors.newCachedThreadPool();

            ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>(1000);
            AtomicInteger atomicInteger = new AtomicInteger();
            String mapKey = fromType + DateUtils.formatDate(localDate);

            LocalDateTime startDateTime = localDate.atTime(LocalTime.of(0, 0, 0));
            LocalDateTime endDateTime = localDate.atTime(LocalTime.of(23, 59, 59));

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
                List<Future<Long>> futureList = articleList.stream().map(esArticle -> {
                    String ossPath = esArticle.getOssPath();
                    String fileName = ossPath.substring(ossPath.indexOf("_") + 1);
                    return executorService.submit(new WriteInLocal(localDate, getArticleJson(esArticle), filePath, fileName, atomicInteger));
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
        ExecutorService executorService = Executors.newCachedThreadPool();
        final LocalDate localDate = LocalDate.now();
        final String articleJson = "fdafsafd";
        final String filePath = "D:\\test\\数据";
        final String fileName = "test.txt";
        AtomicInteger atomicInteger = new AtomicInteger();
        for (int i = 0; i < 1000; i++) {
            executorService.submit(new WriteInLocal(localDate, articleJson, filePath, i + fileName, atomicInteger));

        }
        executorService.shutdown();
    }
}
