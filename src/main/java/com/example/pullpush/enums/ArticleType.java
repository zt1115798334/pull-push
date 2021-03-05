package com.example.pullpush.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleType {

    VIDEO(0, "video", "视频"),
    COMMENT(1, "comment", "评论"),
    TRADITION(99, "tradition", "传统文章");

    private Integer code;
    private String type;
    private String name;
}
