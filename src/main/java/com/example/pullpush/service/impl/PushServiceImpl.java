package com.example.pullpush.service.impl;

import com.example.pullpush.analysis.service.AnalysisService;
import com.example.pullpush.dto.FileInfoDto;
import com.example.pullpush.properties.EsProperties;
import com.example.pullpush.service.PushService;
import com.example.pullpush.service.callable.SendInInterface;
import com.example.pullpush.utils.FileUtils;
import com.google.common.util.concurrent.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class PushServiceImpl implements PushService {

    private final EsProperties esProperties;

    private final AnalysisService analysisService;


    @Override
    public void start() {
        FileUtils txt = new FileUtils(esProperties.getFilePath(), "txt");
        txt.File();
        List<File> fileList = txt.getFileList().stream().filter(Objects::nonNull).collect(Collectors.toList());
        int fileSize = fileList.size();
        log.info("文件总数为：" + fileSize);

        Queue<FileInfoDto> queueRead = new ConcurrentLinkedQueue<>();

        final ExecutorService executorServiceReadSendFile = Executors.newFixedThreadPool(15);
        new Thread(() -> {
            try {
                for (File file : fileList) {
                    Future<FileInfoDto> submit = executorServiceReadSendFile.submit(new ReadFile(file));
                    queueRead.add(submit.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();

        Queue<Long> queueSend = new ConcurrentLinkedQueue<>();
        RateLimiter rateLimiter = RateLimiter.create(100);
        new Thread(() -> {
            try {
                while (true) {
                    while (!queueRead.isEmpty()) {
                        FileInfoDto poll = queueRead.poll();
                        Future<Long> submit = executorServiceReadSendFile.submit(new SendInInterface(rateLimiter, analysisService, poll));
                        queueSend.add(submit.get());
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
        while (!executorServiceReadSendFile.isTerminated()) ;
        executorServiceReadSendFile.shutdown();
    }

    @AllArgsConstructor
    static class ReadFile implements Callable<FileInfoDto> {

        private final File file;

        @Override
        public FileInfoDto call() throws Exception {
            String content = Files.lines(Paths.get(file.getPath()), Charset.defaultCharset()).collect(Collectors.joining(System.getProperty("line.separator")));
            return new FileInfoDto(file.getName(), content);
        }
    }


}
