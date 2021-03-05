package com.example.pullpush.entity;

import lombok.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/17 17:39
 * description:
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GatherWordInfo {

    private Long id;
    
    /**
     * 采集词
     */
    private String name;

    public GatherWordInfo(String name) {
        this.name = name;
    }
}
