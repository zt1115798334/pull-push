package com.example.pullpush.controller;

import com.example.pullpush.base.controller.BaseResultMessage;
import com.example.pullpush.base.controller.ResultMessage;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.handler.SyncPullArticleHandler;
import com.example.pullpush.utils.DateUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
public class PullController extends BaseResultMessage {

    @Resource(name = "customWords")
    private final SyncPullArticleHandler.CustomWords customWords;

    @GetMapping("pullArticleOfCustomWords")
    public ResultMessage pullArticleOfCustomWords(@RequestParam String startDate, @RequestParam String endDate) {
        List<String> wordList = Lists.newArrayList();
        wordList.add("中印冲突,中印关系,中印*边境*冲突,中印*边境*对峙,中印*边界*冲突,中印*边界*对峙");
        wordList.add("中美关系,拜登*对华态度,拜登*谴责中国,拜登*指责中国,中美双边关系");
        wordList.add("后新冠,新冠疫苗*(普及+接种+研发+副作用+有效期+上市+有效+上新+试验+注册+抢跑+打不打+不建议打+不打+不良反应+不能打+获批+临床+研究+投入使用)");
        wordList.add("美国*新冠,美国*肺炎,美国*疫情");
        customWords.handlerData(StorageMode.LOCAL, wordList, DateUtils.parseDate(startDate), DateUtils.parseDate(endDate));
        return success();
    }


}
