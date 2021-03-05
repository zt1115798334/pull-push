package com.example.pullpush.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/21 13:55
 * description: 状态码
 */
@AllArgsConstructor
@Getter
public enum SystemStatusCode {

    SUCCESS(0, "success", "成功"),
    FAILED(1, "failed", "失败"),

    PARAMS_VALIDATION_FAILED(4000, "paramsValidationFailed", "参数异常"),
    ES_ANALYTICAL_ANOMALY(4001, "esAnalyticalAnomaly", "es结果分析异常"),
    ES_RESPONSE_TIMEOUT(5000, "esResponseTimeout", "es接口响应超时"),

    SC_UNAUTHORIZED(401, "unauthorized", "无权限访问"),
    SC_NOT_FOUND(401, "notFound", "没有找到页面"),
    SC_BAD_REQUEST(400, "badRequest", "参数解析失败"),
    SC_METHOD_NOT_ALLOWED(405, "methodNotAllowed", "不支持当前请求方"),
    SC_UNSUPPORTED_MEDIA_TYPE(415, "unsupportedMediaType", "不支持当前媒体类型"),
    SC_INTERNAL_SERVER_ERROR(500, "internalServerError", "系统错误");

    private final Integer code;
    private final String name;
    private final String msg;
}
