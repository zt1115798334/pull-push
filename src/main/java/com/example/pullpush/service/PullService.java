package com.example.pullpush.service;

import com.example.pullpush.entity.GatherWordInfo;
import com.example.pullpush.enums.StorageMode;

import java.time.LocalDate;
import java.util.List;

public interface PullService {

    long pullEsArticle(StorageMode storageMode, List<GatherWordInfo> gatherWordInfos, LocalDate startDate, LocalDate endDate,String fromType);
}
