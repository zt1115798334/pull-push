package com.example.pullpush.custom;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPage<T> {
    private int pageNumber;

    private int pageSize;

    private String scrollId;

    private List<T> list = Collections.emptyList();

    private JSONArray jsonArray = new JSONArray();

    private Long totalElements = 0L;

    public CustomPage(List<T> list, Long totalElements, String scrollId) {
        this.list = list;
        this.totalElements = totalElements;
        this.scrollId = scrollId;
    }

    public CustomPage(JSONArray jsonArray, Long totalElements, String scrollId) {
        this.jsonArray = jsonArray;
        this.totalElements = totalElements;
        this.scrollId = scrollId;
    }

    public CustomPage(int pageNumber, int pageSize, List<T> list, Long totalElements, String scrollId) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.list = list;
        this.totalElements = totalElements;
        this.scrollId = scrollId;
    }
}
