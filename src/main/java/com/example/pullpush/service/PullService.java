package com.example.pullpush.service;

import com.example.pullpush.dto.GatherWordDto;
import com.example.pullpush.enums.StorageMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PullService {

    long pullEsArticleByDay(StorageMode storageMode, List<String> gatherWords, LocalDate startDate, LocalDate endDate, String fromType);

    long pullEsArticleByTimeRange(StorageMode storageMode, List<String> gatherWords, LocalDateTime startDateTime, LocalDateTime endDateTime, String fromType);
}
