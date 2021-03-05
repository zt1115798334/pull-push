package com.example.pullpush.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Carrier {

    CARRIER_NEWS("news", 2001, "新闻", "境外新闻"),
    CARRIER_WE_CHAT("weChat", 2005, "微信"),
    CARRIER_BLOG("blog", 2002, "博客", "境外博客"),
    CARRIER_MICRO_BLOG("microBlog", 2004, "微博"),
    CARRIER_FORUM("forum", 2003, "论坛", "境外论坛"),
    CARRIER_POSTS_BAR("postsBar", 2010, "贴吧"),
    CARRIER_ELECTRONIC_NEWSPAPER("electronicNewspaper", 2007, "电子报"),
    CARRIER_VIDEO("video", 2008, "视频", "境外视频"),
    CARRIER_APP("app", 2009, "APP"),
    CARRIER_INTER_LOCUTION("interLocution", 2011, "问答", "境外问答"),
    CARRIER_COMPREHENSIVE("comprehensive", 2000, "综合"),
    CARRIER_SHORT_VIDEO("shortVideo", 2012, "短视频"),
    CARRIER_OTHER("other", 2999, "其他", "其他"),
    CARRIER_ABROAD_TWITTER("twitter", 3001, "Twitter", "Twitter"),
    CARRIER_ABROAD_FACEBOOK("facebook", 3002, "Facebook", "Facebook");

    private String type;
    private Integer code;
    private String name;
    private String abroadName;

    Carrier(String type, Integer code, String name) {
        this.type = type;
        this.code = code;
        this.name = name;
    }
}