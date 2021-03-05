package com.example.pullpush.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.enums.ArticleType;
import com.example.pullpush.enums.Carrier;
import com.example.pullpush.enums.Emotion;
import com.example.pullpush.es.domain.EsArticle;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/6 17:50
 * description: 文章工具类
 */
public class ArticleUtils {

    /**
     * json 转换 esArticle
     *
     * @param jsonObj json
     * @return EsArticle
     */
    public static EsArticle jsonObjectConvertEsArticle(JSONObject jsonObj) {
        EsArticle esArticle = new EsArticle();
        if (jsonObj != null) {
            esArticle.setId(jsonObj.getString("id"));   //文章id
            esArticle.setRelId(jsonObj.getString("relId"));   //相似文章id
            esArticle.setRegion(jsonObj.getInteger("country"));// 境内外
            esArticle.setCarrier(jsonObj.getString("carrie"));// 载体
            esArticle.setSiteName(jsonObj.getString("siteName"));// 网站/引擎名称
            esArticle.setCreateTime(DateUtils.parseDateTimeUTC(jsonObj.getString("createTime")));// 索引时间
            esArticle.setGatherTime(DateUtils.parseDateTimeUTC(jsonObj.getString("gatherTime")));// 采集时间
            esArticle.setPublishTime(DateUtils.parseDateTimeUTC(jsonObj.getString("publishTime"))); // 发布时间
            esArticle.setUrlMain(jsonObj.getString("urlMain"));// url主域名
            esArticle.setUrl(jsonObj.getString("url"));// 原文地址
            esArticle.setAuthor(jsonObj.getString("author"));// 作者
            esArticle.setFrom(jsonObj.getString("from"));// 转载源 [新闻、视频]
            esArticle.setView(jsonObj.getLong("view")); // 点击数 [新闻、论坛、博客、视频]
            esArticle.setComment(jsonObj.getLong("comment"));// 评论数 [新闻、论坛、博客、视频]
            esArticle.setZan(jsonObj.getLongValue("like"));//点赞数
            esArticle.setIsRelated(jsonObj.getInteger("isrelated")); //是否相关  0 否 1是
            esArticle.setIsOpinion(jsonObj.getInteger("isyuqing")); //是否舆情 0 否 1是
            esArticle.setColumnName(jsonObj.getString("columnName")); //栏目名称


            String emotion = "";
            Integer isNegative = jsonObj.getInteger("isnegative"); //负面
            Integer isNeutral = jsonObj.getInteger("isneutral");  //中性
            Integer isPositive = jsonObj.getInteger("ispositive");    //正面
            if (isNegative == 1 && isNeutral == 0 && isPositive == 0) {
                emotion = Emotion.NEGATIVE.getType();
            }
            if (isNegative == 0 && isNeutral == 1 && isPositive == 0) {
                emotion = Emotion.NEUTRAL.getType();
            }
            if (isNegative == 0 && isNeutral == 0 && isPositive == 1) {
                emotion = Emotion.POSITIVE.getType();
            }

            esArticle.setEmotion(emotion);
            String title = jsonObj.getString("title");
            String cleanTitle = jsonObj.getString("cleanTitle");
            esArticle.setOssPath(jsonObj.getString("ossPath"));// 采集文件的名称
            esArticle.setTitleOriginal(title);
            esArticle.setCleanTitleOriginal(cleanTitle);
            esArticle.setTitle(title);// 原文标题
            esArticle.setCleanTitle(cleanTitle); // 将文章标题中出现的特殊的字符替换掉
            esArticle.setContentWords(jsonObj.getLong("contentWords"));//文章字数
            esArticle.setSummary(jsonObj.getString("summary"));// 摘要
            esArticle.setContent(jsonObj.getString("content"));//内容
            Integer articleType = ArticleType.TRADITION.getCode();
            //短视频
            if (Objects.equals(esArticle.getCarrier(), Carrier.CARRIER_SHORT_VIDEO.getType())) {
                articleType = jsonObj.containsKey("type") && StringUtils.isNotEmpty(jsonObj.getString("type")) ?
                        jsonObj.getIntValue("type") :
                        ArticleType.TRADITION.getCode();
            }
            esArticle.setArticleType(articleType);
            esArticle.setArticleCoverUrl(jsonObj.getString("images"));

            esArticle.setReadUserIds(JSONObject.parseArray(jsonObj.getString("readUsers"), Long.class));// 已读用户的ID数组，分析时默认是空集
            esArticle.setUnRelatedUserIds(JSONObject.parseArray(jsonObj.getString("unRelatedUsers"), Long.class)); // 研判为与我无关的用户的ID数组，分析时默认是空集
            esArticle.setEntityArea(JSONObject.parseArray(jsonObj.getString("entityArea"), String.class));//实体识别地域名
            esArticle.setEntityName(JSONObject.parseArray(jsonObj.getString("entityName"), String.class));//实体识别人名
            esArticle.setEntityOrganization(JSONObject.parseArray(jsonObj.getString("entityOrganization"), String.class));//实体识别组织机构名
            esArticle.setSimilarArticleCount(0L);
            esArticle.setHotWords(JSONObject.parseArray(jsonObj.getString("hotWords"), String.class));
            esArticle.setYuqing(JSONObject.parseArray(jsonObj.getString("yuqing"), Integer.class));
            esArticle.setEntityProvince(JSONObject.parseArray(jsonObj.getString("entityProvince"), String.class));
            esArticle.setEntityCity(JSONObject.parseArray(jsonObj.getString("entityCity"), String.class));
            esArticle.setEntityCounty(JSONObject.parseArray(jsonObj.getString("entityCounty"), String.class));
            esArticle.setPositive(isPositive);
            esArticle.setNegative(isNegative);
            esArticle.setNeutral(isNeutral);
        }
        return esArticle;
    }
}
