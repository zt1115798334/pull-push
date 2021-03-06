package com.example.pullpush.es.service;

import com.alibaba.fastjson.JSONArray;
import com.example.pullpush.custom.CustomPage;
import com.example.pullpush.enums.Carrier;
import com.example.pullpush.enums.SearchModel;
import com.example.pullpush.es.domain.EsArticle;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong date: 2018/8/20 11:24 description: es文章查询的业务层
 */
public interface EsArticleService {

    /**
     * 根据相关词获取所有数据文章列表
     *
     * @return EsPage
     */
    CustomPage<EsArticle> findAllDataEsArticlePage(SearchModel searchModel, JSONArray wordJa, String scrollId,
                                                   LocalDateTime startDateTime,
                                                   LocalDateTime endDateTime, int pageSize,
                                                   List<Carrier> carrier);
}
