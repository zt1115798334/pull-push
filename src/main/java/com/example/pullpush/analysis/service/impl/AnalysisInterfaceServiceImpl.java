package com.example.pullpush.analysis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.analysis.service.AnalysisInterfaceService;
import com.example.pullpush.analysis.url.UrlConstants;
import com.example.pullpush.properties.EsProperties;
import com.example.pullpush.utils.HttpClientUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AnalysisInterfaceServiceImpl implements AnalysisInterfaceService {

    private final EsProperties esProperties;

    @Override
    public void analysis(JSONObject params) {
        HttpClientUtils.getInstance().httpPostFrom(esProperties.getAnalysis() + UrlConstants.URL_ANALYSIS, params.getInnerMap());
    }
}
