package com.example.pullpush.base.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.pullpush.enums.SystemStatusCode;
import com.example.pullpush.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 18:27
 * description: 前后端统一消息定义协议 Message 之后前后端数据交互都按照规定的类型进行交互
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultMessage {

    private final SystemStatusCode SUCCESS = SystemStatusCode.SUCCESS;
    private final SystemStatusCode FAILED = SystemStatusCode.FAILED;
    // 消息头meta 存放状态信息 code message

    @JsonInclude(Include.NON_NULL)
    private Meta meta;
    // 消息内容  存储实体交互数据
    @JsonInclude(Include.NON_NULL)
    private PageData page;

    @JsonInclude(Include.NON_NULL)
    private JSONObject data;

    @JsonInclude(Include.NON_NULL)
    private JSONArray list;

    public ResultMessage setMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public ResultMessage setPageData(PageData page) {
        this.page = page;
        return this;
    }

    public PageData getPage() {
        return this.page;
    }

    public JSONArray findPageDataList() {
        PageData pageData = this.page;
        return pageData != null ? JSONArray.parseArray(JSONArray.toJSONString(pageData.getList())) : new JSONArray();
    }

    public ResultMessage setData(Object data) {
        Object json = JSON.toJSON(data);
        if (json instanceof JSONObject) {
            this.data = JSONObject.parseObject(JSONObject.toJSONStringWithDateFormat(data, JSON.DEFFAULT_DATE_FORMAT));
        }
        if (json instanceof JSONArray) {
            this.list = JSONArray.parseArray(JSONArray.toJSONStringWithDateFormat(data, JSON.DEFFAULT_DATE_FORMAT));
        }
        return this;
    }

    public Object getData() {
        return data;
    }

    public JSONArray getList() {
        return list;
    }

    public ResultMessage correctness() {
        Meta build = Meta.builder().success(Boolean.TRUE).code(SUCCESS.getCode()).timestamp(DateUtils.formatDateTime(DateUtils.currentDateTime())).build();
        return this.setMeta(build);
    }

    public ResultMessage correctness(String statusMsg) {
        Meta build = Meta.builder().success(Boolean.TRUE).code(SUCCESS.getCode()).timestamp(DateUtils.formatDateTime(DateUtils.currentDateTime())).msg(statusMsg).build();
        return this.setMeta(build);
    }

    public ResultMessage correctness(int code, String statusMsg) {
        Meta build = Meta.builder().success(Boolean.TRUE).code(code).timestamp(DateUtils.formatDateTime(DateUtils.currentDateTime())).msg(statusMsg).build();
        return this.setMeta(build);
    }

    public ResultMessage correctnessPage(int pageNumber, int pageSize, long total, Object rows) {
        PageData build = PageData.builder().pageNumber(pageNumber).pageSize(pageSize).total(total).list(rows).build();
        return this.correctness().setPageData(build);
    }

    public ResultMessage error() {
        Meta build = Meta.builder().success(Boolean.FALSE).code(FAILED.getCode()).timestamp(DateUtils.formatDateTime(DateUtils.currentDateTime())).build();
        return this.setMeta(build);
    }

    public ResultMessage error(int statusCode) {
        Meta build = Meta.builder().success(Boolean.FALSE).code(statusCode).timestamp(DateUtils.formatDateTime(DateUtils.currentDateTime())).build();
        return this.setMeta(build);
    }

    public ResultMessage error(String statusMsg) {
        Meta build = Meta.builder().success(Boolean.FALSE).code(FAILED.getCode()).timestamp(DateUtils.formatDateTime(DateUtils.currentDateTime())).msg(statusMsg).build();
        return this.setMeta(build);
    }

    public ResultMessage error(int statusCode, String statusMsg) {
        Meta build = Meta.builder().success(Boolean.FALSE).code(statusCode).timestamp(DateUtils.formatDateTime(DateUtils.currentDateTime())).msg(statusMsg).build();
        return this.setMeta(build);
    }

    public ResultMessage errorPage() {
        PageData build = PageData.builder().pageNumber(0).pageSize(0).total(0).list(Collections.EMPTY_LIST).build();
        return this.error().setPageData(build);
    }


    @Getter
    @Builder()
    @JsonIgnoreProperties
    public static class Meta {
        private final boolean success;
        private final int code;
        private final String timestamp;
        private final String msg;
    }

    @Getter
    @Builder()
    @JsonIgnoreProperties
    public static class PageData {
        private final int pageNumber;
        private final int pageSize;
        private final long total;
        private final Object list;
    }
}
