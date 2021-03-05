package com.example.pullpush.handler;

import com.example.pullpush.entity.GatherWordInfo;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.service.PullService;
import com.example.pullpush.utils.MStringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fan on 7/29/20.
 */
@Component
@AllArgsConstructor
public class SyncPullArticleHandler {

    @Component("customWords")
    @AllArgsConstructor
    public static class CustomWords {

        private final PullService pullEsArticle;

        public long handlerData(StorageMode storageMode, List<String> wordList, LocalDate startDate, LocalDate endDate) {
            List<GatherWordInfo> gatherWordInfos = wordList.stream().map(MStringUtils::splitMinGranularityStr)
                    .flatMap(Collection::stream)
                    .distinct().map(s -> GatherWordInfo.builder().name(s).build()).collect(Collectors.toList());
            return pullEsArticle.pullEsArticle(storageMode, gatherWordInfos, startDate, endDate, "fromType");
        }
    }
}
