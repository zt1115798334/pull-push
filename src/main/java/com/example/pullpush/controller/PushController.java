package com.example.pullpush.controller;

import com.example.pullpush.base.controller.BaseResultMessage;
import com.example.pullpush.base.controller.ResultMessage;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.handler.SyncPullArticleHandler;
import com.example.pullpush.service.PushService;
import com.example.pullpush.utils.DateUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 9:48
 * description: 应用中心
 */

@AllArgsConstructor
@RestController
@RequestMapping("api")
public class PushController extends BaseResultMessage {

    private final PushService pushService;

    @GetMapping("pushArticle")
    public ResultMessage pushArticle() throws ExecutionException, InterruptedException {
        pushService.start();
        return success();
    }
}
