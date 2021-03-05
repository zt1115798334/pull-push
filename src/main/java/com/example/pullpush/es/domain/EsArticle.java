package com.example.pullpush.es.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/22 9:29
 * description: es文章
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsArticle {

    /**
     * 文章id
     */
    private String id;
    /**
     * 相似文章id
     */
    private String relId;

    /**
     * 区域：0 全部1 境内 2 境外
     */
    private Integer region;

    /**
     * 载体
     */
    private String carrier;

    /**
     * 网站
     */
    private String siteName;
    
    /**
     * 栏目名称
     */
    private String columnName;

    /**
     * 网站标识
     */
    private String siteNameType;

    /**
     * 索引时间
     */
    private LocalDateTime createTime;

    /**
     * 采集时间
     */
    private LocalDateTime gatherTime;

    /**
     * 发布日期
     */
    private LocalDate publishDate;
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * url主域名
     */
    private String urlMain;

    /**
     * url
     */
    private String url;

    /**
     * 作者
     */
    private String author;

    /**
     * 转载源
     */
    private String from;

    /**
     * 点击数
     */
    private Long view;

    /**
     * 评论数
     */
    private Long comment;

    /**
     * 点赞数
     */
    private Long zan;

    /**
     * 是否相关 0 否  1是
     */
    private Integer isRelated;

    /**
     * 是否舆情 0 否 1 是
     */
    private Integer isOpinion;

    /**
     * 正面：positive, 负面：negative 中性 neutral
     */
    private String emotion;

    /**
     * 采集文件的名称
     */
    private String ossPath;

    /**
     * 原文标题 -- 原始
     */
    private String titleOriginal;

    /**
     * 将文章标题中出现的特殊的字符替换掉 -- 原始
     */
    private String cleanTitleOriginal;
    /**
     * 原文标题
     */
    private String title;

    /**
     * 将文章标题中出现的特殊的字符替换掉
     */
    private String cleanTitle;

    /**
     * 文章内容字数
     */
    private Long contentWords;

    /**
     * 摘要-- 原始
     */
    private String summaryOriginal;

    /**
     * 正文内容-- 原始
     */
    private String contentOriginal;
    /**
     * 摘要
     */
    private String summary;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 文章类型
     */
    private Integer articleType;

    /**
     * 文章封面
     */
    private String articleCoverUrl;

    /**
     * 阅读人员id
     */
    private List<Long> readUserIds;

    /**
     * 无关人员id
     */
    private List<Long> unRelatedUserIds;

    /**
     * 地址
     */
    private List<String> entityArea;

    /**
     * 人员
     */
    private List<String> entityName;

    /**
     * 组织
     */
    private List<String> entityOrganization;

    /**
     * 相似文章数量
     */
    private Long similarArticleCount;
    
    /**
     * 热词
     */
    private List<String> hotWords;
    /**
     * 舆情
     */
    private List<Integer> yuqing;
    
    /**
     * 省份
     */
    private List<String> entityProvince;
    
    /**
     * 城市
     */
    private List<String> entityCity;
    /**
     * 乡镇
     */
    private List<String> entityCounty;
    /**
     * 正面
     */
    private Integer positive;
    /**
     * 负面
     */
    private Integer negative;
    /**
     * 中性
     */
    private Integer neutral;
    
    

    ///////////////////////////////////////////////////////////////////////////
    // 以下是丰富的属性
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 预警等级  red 红色 ，orange 橙色，yellow黄色
     */
    private String warningLevel;

    /**
     * 预警时间
     */
    private LocalDateTime warningTime;

    /**
     * 加入报告信息
     */
    private List<JSONObject> joinBriefingInfo;


    /**
     * 阅读状态
     */
    private boolean readState;

    /**
     * 收藏状态
     */
    private boolean favoriteState;


    /**
     * 收藏时间
     */
    private LocalDateTime favoriteTime;

    /**
     * 是否是境外
     */
    private boolean isAbroad;

    /**
     * 文章封面base64
     */
    private String articleCoverBase64;

    public EsArticle(String id, Integer region, String carrier,
                     String siteName, LocalDateTime createTime, LocalDateTime gatherTime, LocalDateTime publishTime,
                     String urlMain, String url, String author, String from,
                     Long view, Long comment, Integer isRelated, Integer isOpinion,
                     String emotion, String ossPath, String title, String cleanTitle,
                     Long contentWords, String summary, String content,
                     List<Long> readUserIds, List<Long> unRelatedUserIds,
                     List<String> entityArea, List<String> entityName, List<String> entityOrganization) {
        this.id = id;
        this.region = region;
        this.carrier = carrier;
        this.siteName = siteName;
        this.createTime = createTime;
        this.gatherTime = gatherTime;
        this.publishTime = publishTime;
        this.urlMain = urlMain;
        this.url = url;
        this.author = author;
        this.from = from;
        this.view = view;
        this.comment = comment;
        this.isRelated = isRelated;
        this.isOpinion = isOpinion;
        this.emotion = emotion;
        this.ossPath = ossPath;
        this.title = title;
        this.cleanTitle = cleanTitle;
        this.contentWords = contentWords;
        this.summary = summary;
        this.content = content;
        this.readUserIds = readUserIds;
        this.unRelatedUserIds = unRelatedUserIds;
        this.entityArea = entityArea;
        this.entityName = entityName;
        this.entityOrganization = entityOrganization;
    }
}
