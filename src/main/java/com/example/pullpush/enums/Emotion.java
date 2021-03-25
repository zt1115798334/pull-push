package com.example.pullpush.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Emotion {

    POSITIVE("positive", "正面"),
    NEGATIVE("negative", "负面"),
    NEUTRAL("neutral", "中性"),
    IRRELEVANT("irrelevant", "无关");

    private final String type;
    private final String name;

}