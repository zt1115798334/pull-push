package com.example.pullpush.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.base.controller.BaseResultMessage;
import com.example.pullpush.base.controller.ResultMessage;
import com.example.pullpush.mysql.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AnTest extends BaseResultMessage {

    private final AuthorService authorService;

    @PostMapping("/analysis/analysis/put")
    public ResultMessage analysis(@RequestParam String data, @RequestParam String fileName) {
        System.out.println("fileName = " + fileName);
        JSONObject dataParams = JSON.parseObject(data);
        String author = dataParams.getString("Author");
        System.out.println("作者："+ author +" 存在状态："+authorService.isExistsAuthor(author));
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return success();
    }
}
