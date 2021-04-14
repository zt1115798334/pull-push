package com.example.pullpush.controller;

import com.example.pullpush.base.controller.BaseResultMessage;
import com.example.pullpush.base.controller.ResultMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnTest extends BaseResultMessage {

    @PostMapping("/analysis/analysis/put")
    public ResultMessage analysis(@RequestParam String data, @RequestParam String fileName) {
        System.out.println("fileName = " + fileName);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return success();
    }
}
