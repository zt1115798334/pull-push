package com.example.pullpush.service;

import com.example.pullpush.custom.RichParameters;
import com.example.pullpush.enums.StorageMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PullService {

    long pullEsArticleByDateRange(RichParameters richParameters, List<String> words, LocalDate startDate, LocalDate endDate);

    long pullEsArticleByTimeRange(RichParameters richParameters, List<String> words, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
