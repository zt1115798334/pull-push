package com.example.pullpush.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.base.handler.page.PageHandler;
import com.example.pullpush.custom.RichParameters;
import com.example.pullpush.dto.GatherWordDto;
import com.example.pullpush.enums.SearchModel;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.mysql.entity.Author;
import com.example.pullpush.mysql.service.AuthorService;
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

    @Component("customAuthorsByDateRange")
    @AllArgsConstructor
    public static class CustomAuthorsByDateRange {

        private final PullService pullEsArticle;
        private final CustomWordProperties customWordProperties;

        public long handlerData(JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            List<String> authors = customWordProperties.getAuthor().stream()
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.AUTHOR)
                    .fromType("custom")
                    .carrier(customWordProperties.getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, authors, startDate, endDate);
        }
    }

    @Component("queryWordsByDateRange")
    @AllArgsConstructor
    public static class QueryWordsByDateRange {

        private final PullService pullEsArticle;

        public long handlerData(JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            JSONArray queryWords = extraParams.getJSONArray("queryWords");
            List<String> gatherWords =queryWords.stream().map(String::valueOf)
                    .map(MStringUtils::splitMinGranularityStr)
                    .flatMap(Collection::stream)
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .fromType("custom")
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, gatherWords, startDate, endDate);
        }
    }

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
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .fromType("custom")
                    .carrier(customWordProperties.getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, gatherWords, startDate, endDate);
        }
    }

    @Component("gatherAuthorsByDateRange")
    @AllArgsConstructor
    public static class GatherAuthorsByDateRange extends PageHandler<Author> {

        private final PullService pullEsArticle;

        private final AuthorService authorService;

        private final CustomWordProperties customWordProperties;

        @Override
        protected long handleDataOfPerPage(List<Author> list, int pageNumber, JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            List<String> authors = list.parallelStream().map(Author::getAuthorName)
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.AUTHOR)
                    .fromType("custom")
                    .carrier(customWordProperties.getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, authors, startDate, endDate);
        }

        @Override
        protected Page<Author> getPageList(int pageNumber, JSONObject extraParams) {
            return authorService.findPageByEntity(pageNumber, DEFAULT_BATCH_SIZE);
        }
    }

    @Component("gatherWordsByDateRange")
    @AllArgsConstructor
    public static class GatherWordsByDateRange extends PageHandler<GatherWordDto> {

        private final PullService pullEsArticle;

        private final GatherWordsService gatherWordsService;

        private final CustomWordProperties customWordProperties;


        @Override
        protected long handleDataOfPerPage(List<GatherWordDto> list, int pageNumber, JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDate startDate = extraParams.getObject("startDate", LocalDate.class);
            LocalDate endDate = extraParams.getObject("endDate", LocalDate.class);
            List<String> gatherWords = list.stream().map(GatherWordDto::getName).collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .fromType("gather")
                    .carrier(customWordProperties.getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByDateRange(richParameters, gatherWords, startDate, endDate);
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

    @Component("customAuthorsByTimeRange")
    @AllArgsConstructor
    public static class CustomAuthorsByTimeRange {

        private final PullService pullEsArticle;

        private final CustomWordProperties customWordProperties;

        public long handlerData(JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDateTime startDateTime = extraParams.getObject("startDateTime", LocalDateTime.class);
            LocalDateTime endDateTime = extraParams.getObject("endDateTime", LocalDateTime.class);
            String fromType = extraParams.getString("fromType");
            List<String> authors = customWordProperties.getAuthor().stream()
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.AUTHOR)
                    .fromType(fromType)
                    .carrier(customWordProperties.getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByTimeRange(richParameters, authors, startDateTime, endDateTime);
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
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .carrier(customWordProperties.getCarrier())
                    .fromType(fromType).build();
            return pullEsArticle.pullEsArticleByTimeRange(richParameters, gatherWords, startDateTime, endDateTime);
        }
    }

    @Component("gatherAuthorsByTimeRange")
    @AllArgsConstructor
    public static class GatherAuthorsByTimeRange extends PageHandler<Author> {

        private final PullService pullEsArticle;

        private final AuthorService authorService;

        private final CustomWordProperties customWordProperties;

        @Override
        protected long handleDataOfPerPage(List<Author> list, int pageNumber, JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDateTime startDateTime = extraParams.getObject("startDateTime", LocalDateTime.class);
            LocalDateTime endDateTime = extraParams.getObject("endDateTime", LocalDateTime.class);
            String fromType = extraParams.getString("fromType");
            List<String> authors = list.parallelStream().map(Author::getAuthorName)
                    .distinct().collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder().storageMode(storageMode)
                    .searchModel(SearchModel.AUTHOR)
                    .fromType(fromType)
                    .carrier(customWordProperties.getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByTimeRange(richParameters, authors, startDateTime, endDateTime);
        }

        @Override
        protected Page<Author> getPageList(int pageNumber, JSONObject extraParams) {
            return authorService.findPageByEntity(pageNumber, DEFAULT_BATCH_SIZE);
        }
    }

    @Component("gatherWordsByTimeRange")
    @AllArgsConstructor
    public static class GatherWordsByTimeRange extends PageHandler<GatherWordDto> {

        private final PullService pullEsArticle;

        private final GatherWordsService gatherWordsService;

        private final CustomWordProperties customWordProperties;

        @Override
        protected long handleDataOfPerPage(List<GatherWordDto> list, int pageNumber, JSONObject extraParams) {
            log.info("extraParams:{}", extraParams.toJSONString());
            log.info("GatherWordDto:{}", list);
            StorageMode storageMode = extraParams.getObject("storageMode", StorageMode.class);
            LocalDateTime startDateTime = extraParams.getObject("startDateTime", LocalDateTime.class);
            LocalDateTime endDateTime = extraParams.getObject("endDateTime", LocalDateTime.class);
            String fromType = extraParams.getString("fromType");
            List<String> gatherWords = list.stream().map(GatherWordDto::getName).collect(Collectors.toList());
            RichParameters richParameters = RichParameters.builder()
                    .storageMode(storageMode)
                    .searchModel(SearchModel.RELATED_WORDS)
                    .fromType(fromType)
                    .carrier(customWordProperties.getCarrier())
                    .build();
            return pullEsArticle.pullEsArticleByTimeRange(richParameters, gatherWords, startDateTime, endDateTime);
        }

        @Override
        protected Page<GatherWordDto> getPageList(int pageNumber, JSONObject extraParams) {
            if (extraParams.getBooleanValue("status")) {
                return gatherWordsService.findPageByEntityStatus(1L, pageNumber, DEFAULT_BATCH_SIZE);
            } else {
                return gatherWordsService.findPageByEntity(pageNumber, DEFAULT_BATCH_SIZE);
            }
        }
    }

    public static void main(String[] args) {
        JSONObject j = new JSONObject();
        System.out.println(j.getBoolean("dd"));
    }
}
