package com.example.pullpush.service.callable;

import com.example.pullpush.analysis.service.AnalysisService;
import com.example.pullpush.dto.FileInfoDto;
import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
@AllArgsConstructor
public class SendInInterface  implements Callable<Long> {
    private final RateLimiter rateLimiter;

    private final AnalysisService analysisService;

    private final FileInfoDto fileInfoDto;

    @Override
    public Long call() {
        long start = System.currentTimeMillis();
        rateLimiter.acquire();
        analysisService.analysis(fileInfoDto.getContent(), fileInfoDto.getFilename());
        long end = System.currentTimeMillis();
        return end - start;
    }
}
