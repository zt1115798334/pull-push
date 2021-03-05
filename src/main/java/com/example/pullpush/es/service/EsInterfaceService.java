package com.example.pullpush.es.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/22 10:55
 * description: es接口业务层
 */
public interface EsInterfaceService{
    /**
     * 全文检索接口  分页
     *
     * @param params     参数
     * @param pageNumber 页数
     * @param pageSize   每页显示数量
     * @return String
     */
    String dataQuery(JSONObject params, int pageSize);
}
