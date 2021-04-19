package com.example.pullpush.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.analysis.service.AnalysisService;
import com.example.pullpush.dto.FileInfoDto;
import com.example.pullpush.enums.ReadModel;
import com.example.pullpush.mysql.entity.ArticleFile;
import com.example.pullpush.mysql.service.ArticleFileService;
import com.example.pullpush.properties.EsProperties;
import com.example.pullpush.service.PushService;
import com.example.pullpush.service.callable.SendDbInInterface;
import com.example.pullpush.service.callable.SendInInterface;
import com.example.pullpush.utils.FileUtils;
import com.google.common.collect.Lists;
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

    private final ArticleFileService articleFileService;

    @Override
    public void start() {
        if (Objects.equals(esProperties.getReadModel(), ReadModel.File)) {
            readFile();
        } else {
            readDB();
        }

    }

    public void readDB() {
        long articleFileCount = articleFileService.findCount();

        int pageSize = 10;
        long pageTotal = articleFileCount / pageSize;

        final ExecutorService executorServiceReadSendFile = Executors.newFixedThreadPool(15);
        Queue<FileInfoDto> queueRead = new ConcurrentLinkedQueue<>();
        for (int i = 1; i < pageTotal + 2; i++) {
            final int iFinal = i;
            Future<List<ArticleFile>> listFuture = executorServiceReadSendFile.submit(() -> {
                long start = System.currentTimeMillis();
                List<ArticleFile> content = articleFileService.findPage(iFinal, pageSize).getContent();
                long end = System.currentTimeMillis();
                System.out.println("ThreadName:" + Thread.currentThread().getName() + " time:" + (end - start) + " 数量为:" + content.size());
                return content;
            });
            try {
                List<ArticleFile> articleFileList = listFuture.get();
                articleFileList.parallelStream().map(articleFile -> {
                            String id = articleFile.getId();
                            String fileContext = articleFile.getFileContext();
                            JSONObject jsonObject = JSONObject.parseObject(fileContext);
                            StringBuilder sb = new StringBuilder(id);
                            String Carrie = jsonObject.getString("Carrie");
                            String Profession = jsonObject.getString("Profession");
                            sb.insert(9, Carrie + "_" + Profession + "_");
                            return new FileInfoDto(sb.toString() + ".txt", articleFile.getFileContext());
                        }
                ).forEach(queueRead::add);
                log.info("第{}页，获取完数据", i);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
        System.out.println("当前队列数量:" + queueRead.size());
        Queue<Long> queueSend = new ConcurrentLinkedQueue<>();
        RateLimiter rateLimiter = RateLimiter.create(100);

        try {
            while (!queueRead.isEmpty()) {
                FileInfoDto poll = queueRead.poll();
                Future<Long> submit = executorServiceReadSendFile.submit(new SendInInterface(rateLimiter, analysisService, poll));
                queueSend.add(submit.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executorServiceReadSendFile.shutdown();
        while (true) {
            if (executorServiceReadSendFile.isTerminated()) {
                System.out.println("所有的子线程都结束了！");
                break;
            }
        }
        articleFileService.truncateTable();

    }

    public void readFile() {
        Queue<FileInfoDto> queueRead = new ConcurrentLinkedQueue<>();
        FileUtils txt = new FileUtils(esProperties.getFilePath(), "txt");
        txt.File();
        List<File> fileList = txt.getFileList().stream().filter(Objects::nonNull).collect(Collectors.toList());
        int fileSize = fileList.size();
        log.info("文件总数为：" + fileSize);


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
        executorServiceReadSendFile.shutdown();
        while (true) {
            if (executorServiceReadSendFile.isTerminated()) {
                System.out.println("所有的子线程都结束了！");
                break;
            }
        }
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

    public static void main(String[] args) {
        File f = new File("E:\\1122");//获取路径  F:\测试目录
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String s = file.getName().split("_")[0];
            System.out.print("ID = " + s + " or ");
        }
    }

    public static void moreThread() {
        StringBuilder sb = new StringBuilder("19700101_5cb843af8e45baaf49a69486032b4dc5");
        String Carrie = "2001";
        String Profession = "1000";
        sb.insert(9, Carrie + "_" + Profession + "_");
        System.out.println("Profession = " + sb.toString());
    }

}
