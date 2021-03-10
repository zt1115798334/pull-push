package com.example.pullpush.base.handler.page;

import com.example.pullpush.custom.SysConst;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/4/24 16:34
 * description:
 */
@Slf4j
public abstract class PageThreadHandler<T> {

    protected final int DEFAULT_BATCH_SIZE = SysConst.DEFAULT_BATCH_SIZE;


    public void handle() {
        try {
            log.info("处理数据开始");
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("demo-pool-%d").build();
            ExecutorService executor = new ThreadPoolExecutor(10, 12,
                    1L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
            int total = 0;
            int count = handleData(executor);
            total += count;
            executor.shutdownNow();

            log.info("共处理了{}条数据", total);
            log.info("处理数据结束");
        } catch (Exception e) {
            log.error("处理数据出错，异常信息：{} ", e.getMessage());
        }
    }

    private int handleData(ExecutorService executor) {
        int total = 0;
        int pageNumber = 1;

        Page<T> page = getPageList(pageNumber);
        long totalPages = page.getTotalPages();
        List<T> list = page.getContent();

        log.info("第1页数据处理开始");
        int count = handleDataOfPerPage(list, pageNumber, executor);
        log.info("第1页数据处理结束，处理了{}条数据", count);
        total += count;

        for (int i = 2; i <= totalPages; i++) {
            page = getPageList(i);
            list = page.getContent();
            log.info("第{}页数据处理开始", i);
            count = handleDataOfPerPage(list, i, executor);
            log.info("第{}页数据处理结束，处理了{}条数据", i, count);
            total += count;
        }
        return total;
    }

    protected abstract int handleDataOfPerPage(List<T> list, int pageNumber,
                                               ExecutorService executor);

    protected abstract Page<T> getPageList(int pageNumber);
}
