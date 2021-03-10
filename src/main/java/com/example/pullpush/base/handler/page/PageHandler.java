package com.example.pullpush.base.handler.page;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.custom.SysConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/24 14:58
 * description:
 */
@Slf4j
public abstract class PageHandler<T> {


    protected final int DEFAULT_BATCH_SIZE = SysConst.DEFAULT_BATCH_SIZE;


    public void handle(JSONObject extraParams) {
        try {
            long s = System.currentTimeMillis();
            @SuppressWarnings("unused")
            int count = handleData(extraParams);

            log.info("处理数据结束,数据处理时间:[" + (System.currentTimeMillis() - s) + "]ms");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("处理数据出错，异常信息：{} ", e.toString());
        }
    }

    private int handleData(JSONObject extraParams) {
        int total = 0;
        int pageNumber = 1;

        Page<T> page = getPageList(pageNumber, extraParams);

        long totalPages = page.getTotalPages();
        long totalCounts = page.getTotalElements();
        List<T> list = page.getContent();
        log.info("采集词共{}个,每次批量获取{}词的数据,共分{}次获取", totalCounts, SysConst.DEFAULT_BATCH_SIZE, totalPages);
        log.info("第1批数据处理开始");
        long s = System.currentTimeMillis();
        long count = handleDataOfPerPage(list, pageNumber, extraParams);
        log.info("第1页数据处理结束，处理了" + count + "条数据，数据处理时间:[" + (System.currentTimeMillis() - s) + "]ms");
        total += count;

        for (int i = 2; i <= totalPages; i++) {
            page = getPageList(i, extraParams);

            list = page.getContent();
            log.info("第{}页数据处理开始", i);
            count = handleDataOfPerPage(list, i, extraParams);
            log.info("第" + i + "页数据处理结束，处理了" + count + "条数据，数据处理时间:[" + (System.currentTimeMillis() - s) + "]ms");
            total += count;
        }
        return total;
    }

    protected abstract long handleDataOfPerPage(List<T> list, int pageNumber, JSONObject params);

    protected abstract Page<T> getPageList(int pageNumber,JSONObject extraParams);
}
