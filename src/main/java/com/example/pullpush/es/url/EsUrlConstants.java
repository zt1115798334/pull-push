package com.example.pullpush.es.url;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 11:28
 * description: es链接
 */
public abstract class EsUrlConstants {

    public final static String URL_FULL_QUERY = "/restserver/index/query/fullQuery"; //全文检索 -- 包含文章详情
    public final static String URL_DATA_QUERY = "/restserver/scroll/dataQuery"; //全文检索 -- 包含文章详情
}
