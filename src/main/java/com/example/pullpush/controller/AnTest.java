package com.example.pullpush.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.base.controller.BaseResultMessage;
import com.example.pullpush.base.controller.ResultMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnTest extends BaseResultMessage {

    @PostMapping("antest")
    public ResultMessage antest(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject.toJSONString().length());
        return success();
    }
}
