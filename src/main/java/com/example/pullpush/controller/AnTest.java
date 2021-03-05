package com.example.pullpush.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.base.controller.BaseResultMessage;
import com.example.pullpush.base.controller.ResultMessage;
import com.example.pullpush.enums.StorageMode;
import com.example.pullpush.utils.DateUtils;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnTest extends BaseResultMessage {

    @PostMapping("antest")
    public ResultMessage antest(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject.toJSONString());
        return success();
    }
}
