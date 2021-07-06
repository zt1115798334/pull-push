package com.example.pullpush.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Carrier {

    NEWS("news", 2001, "新闻"),
    WE_CHAT("weChat", 2005, "微信"),
    BLOG("blog", 2002, "博客"),
    MICRO_BLOG("microBlog", 2004, "微博"),
    FORUM("forum", 2003, "论坛"),
    POSTS_BAR("postsBar", 2010, "贴吧"),
    ELECTRONIC_NEWSPAPER("electronicNewspaper", 2007, "电子报"),
    VIDEO("video", 2008, "视频"),
    APP("app", 2009, "APP"),
    INTER_LOCUTION("interLocution", 2011, "问答"),
    COMPREHENSIVE("comprehensive", 2000, "综合"),
    SHORT_VIDEO("shortVideo", 2012, "短视频"),
    OTHER("other", 2999, "其他"),
    ABROAD_TWITTER("twitter", 3001, "Twitter"),
    ABROAD_FACEBOOK("facebook", 3002, "Facebook");

    private final String type;
    private final Integer code;
    private final String name;

}