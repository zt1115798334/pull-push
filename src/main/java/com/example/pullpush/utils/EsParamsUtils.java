package com.example.pullpush.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/1/26 13:15
 * description:
 */
public class EsParamsUtils {

    private static final String ES_SCROLL_ID= "scrollId";
    public static final String ES_START_TIME = "start_time"; //开始时间
    public static final String ES_END_TIME = "end_time"; //结束时间
    public static final String ES_GATHER_START_TIME = "gather_start_time"; //开始时间
    public static final String ES_GATHER_END_TIME = "gather_end_time"; //结束时间
    public static final String ES_CREATE_START_TIME = "create_start_time"; //开始时间
    public static final String ES_CREATE_END_TIME = "create_end_time"; //结束时间
    private static final String ES_RELATED_WORDS = "relatedWords"; //相关词
    public static final String ES_THESAURUS_THEME_IDS = "yuqing"; //主题id
    public static final String ES_URL_MAIL = "urlMain";
    public static final String CARRIE = "carrie";
    private static final String ES_AUTHOR = "author"; //作者
    public static final String ES_COLUMN = "channel";
    public static final String ES_SITE_NAME = "siteName";
    
    private static final String ES_SEARCH_WORDS = "searchValue"; //相关词

    /**
     * 处理统一参数   queryParams 内部使用的参数
     *
     * @return JSONObject
     */
    public static JSONObject getQuerySearchValueParams(JSONArray related) {
        JSONObject csJo = new JSONObject();
        csJo.put(ES_SEARCH_WORDS, JSONObject.parseArray(related.toJSONString(), String.class));
        return csJo;
    }
    public static JSONObject getQueryRelatedWordsParams(JSONArray related) {
        JSONObject csJo = new JSONObject();
        csJo.put(ES_RELATED_WORDS, JSONObject.parseArray(related.toJSONString(), String.class));
        return csJo;
    }
    public static JSONObject getQueryAuthor(JSONArray authorList) {
        JSONObject param = new JSONObject();
        param.put(ES_AUTHOR, JSONObject.parseArray(authorList.toJSONString(), String.class));
        return param;
    }


    
    /**
     * 载体参数
     *
     * @param carrie
     * @return JSONObject
     */
    public static JSONObject getQueryCarrieParams(List<Integer> carrie) {
        JSONObject csJo = new JSONObject();
        csJo.put(CARRIE, carrie);
        return csJo;
    }
    
    /**
     * 站点参数
     *
     * @param siteName
     * @return JSONObject
     */
    public static JSONObject getQuerySiteNameParams(List<String> siteName) {
        JSONObject csJo = new JSONObject();
        csJo.put(ES_SITE_NAME, siteName);
        return csJo;
    }

    /**
     * 时间参数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return JSONObject
     */
    public static JSONObject getQueryTimeParams(LocalDateTime startTime, LocalDateTime endTime) {
        JSONObject csJo = new JSONObject();
        csJo.put(ES_START_TIME, DateUtils.formatDateTime(startTime));
        csJo.put(ES_END_TIME, DateUtils.formatDateTime(endTime));
        return csJo;
    }

    public static JSONObject getQueryScrollIdParams(String scrollId) {
        JSONObject csJo = new JSONObject();
        csJo.put(ES_SCROLL_ID, scrollId);
        return csJo;
    }
}
