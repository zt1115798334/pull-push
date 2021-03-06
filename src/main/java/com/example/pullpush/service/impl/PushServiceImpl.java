package com.example.pullpush.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.properties.EsProperties;
import com.example.pullpush.service.PushService;
import com.example.pullpush.utils.FileUtils;
import com.example.pullpush.utils.HttpClientUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
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

    @Override
    public void start() {
        String analysis = esProperties.getAnalysis();
        FileUtils txt = new FileUtils(esProperties.getFilePath(), "txt");
        txt.File();
        List<File> fileList = txt.getFileList().stream().filter(Objects::nonNull).collect(Collectors.toList());
        int fileSize = fileList.size();
        log.info("文件总数为：" + fileSize);

        Queue<FileInfo> queue = new ConcurrentLinkedQueue<>();

        final ExecutorService executorServiceReadSendFile = Executors.newFixedThreadPool(15);
        new Thread(() -> {
            try {
                for (File file : fileList) {
                    Future<FileInfo> submit = executorServiceReadSendFile.submit(new ReadFile(file));
                    queue.add(submit.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            while (true) {
                while (!queue.isEmpty()) {
                    FileInfo poll = queue.poll();
                    Future<Long> submit = executorServiceReadSendFile.submit(new SendFile(analysis, poll));
                }
            }
        }).start();
        while (!executorServiceReadSendFile.isTerminated()) ;
        executorServiceReadSendFile.shutdown();
    }

    @Data
    @AllArgsConstructor
    static class FileInfo {

        private final String filename;

        private final String content;

    }

    @AllArgsConstructor
    static class ReadFile implements Callable<FileInfo> {

        private final File file;

        @Override
        public FileInfo call() throws Exception {
            String content = Files.lines(Paths.get(file.getPath()), Charset.defaultCharset()).collect(Collectors.joining(System.getProperty("line.separator")));
            return new FileInfo(file.getName(), content);
        }
    }

    @AllArgsConstructor
    static class SendFile implements Callable<Long> {

        private final String url;

        private final FileInfo fileInfo;

        @Override
        public Long call() {
            long start = System.currentTimeMillis();
            JSONObject params = new JSONObject();
            params.put("data", fileInfo.getContent());
            params.put("fileName", fileInfo.getFilename());
            String msg = HttpClientUtils.getInstance().httpPostJson(url, params.getInnerMap());
            System.out.println("msg = " + msg);
            long end = System.currentTimeMillis();
            return end - start;
        }
    }

}
