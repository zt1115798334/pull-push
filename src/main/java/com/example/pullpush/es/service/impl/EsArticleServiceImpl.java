package com.example.pullpush.es.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.example.pullpush.custom.CustomPage;
import com.example.pullpush.enums.SearchModel;
import com.example.pullpush.enums.SearchType;
import com.example.pullpush.es.domain.EsArticle;
import com.example.pullpush.es.service.EsArticleService;
import com.example.pullpush.es.service.EsInterfaceService;
import com.example.pullpush.properties.EsProperties;
import com.example.pullpush.utils.ArticleUtils;
import com.example.pullpush.utils.EsParamsUtils;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 11:31
 * description:
 */
@Slf4j
@AllArgsConstructor
@Service
public class EsArticleServiceImpl implements EsArticleService {

    private final EsInterfaceService esInterfaceService;

    private final EsProperties esProperties;

    private List<EsArticle> jsonToArticleList(JSONArray jsonArray) {
        return Optional.ofNullable(jsonArray)
                .map(obj -> obj.stream()
                        .map(o -> TypeUtils.castToJavaBean(o, JSONObject.class))
                        .map(ArticleUtils::jsonObjectConvertEsArticle).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public boolean isJsonObject(String content) {
        if (StringUtils.isBlank(content))
            return false;
        try {
            @SuppressWarnings("unused")
            JSONObject json = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private CustomPage<EsArticle> jsonToArticlePage(String str) {
        if (!isJsonObject(str)) {
            log.info("返回数据解析错误:" + str);
            return new CustomPage<>();
        }
        return Optional.ofNullable(JSONObject.parseObject(str))
                .map(jo -> {
                    if (jo.getIntValue("code") != 0) {
                        log.info("接口数据查询错误:" + str);
                        return new CustomPage<EsArticle>();
                    }
                    List<EsArticle> rows = jsonToArticleList(jo.getJSONArray("result"));
                    long total = jo.getLongValue("count");
                    String scrollId = jo.getString("scrollId");
                    return new CustomPage<>(rows, total, scrollId);
                })
                .orElse(new CustomPage<>());
    }

    @Override
    public CustomPage<EsArticle> findAllDataEsArticlePage(SearchModel searchModel, JSONArray wordJa,
                                                          String scrollId,
                                                          LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                          int pageSize) {
        System.out.println("searchModel = " + searchModel);
        JSONObject params = new JSONObject();
        if (Objects.equal(searchModel, SearchModel.RELATED_WORDS)) {
            if (Objects.equal(esProperties.getSearchType(), SearchType.EXACT)) {
                params.putAll(EsParamsUtils.getQueryRelatedWordsParams(wordJa));
            }else{
                params.putAll(EsParamsUtils.getQuerySearchValueParams(wordJa));
            }
        } else if (Objects.equal(searchModel, SearchModel.AUTHOR)) {
            params.putAll(EsParamsUtils.getQueryAuthor(wordJa));
        }
        params.putAll(EsParamsUtils.getQueryScrollIdParams(scrollId));
        params.putAll(EsParamsUtils.getQueryTimeParams(startDateTime, endDateTime));
        params.put("searchType", "all");
        String str = esInterfaceService.dataQuery(params, pageSize);
        return jsonToArticlePage(str);
    }
}
