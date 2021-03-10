package com.example.pullpush.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.analysis.service.AnalysisInterfaceService;
import com.example.pullpush.analysis.service.AnalysisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisInterfaceService analysisInterfaceService;

    @Override
    public void analysis(String data, String fileName) {
        JSONObject params = new JSONObject();
        params.put("data", data);
        params.put("fileName", fileName);
        analysisInterfaceService.analysis(params);
    }
}
