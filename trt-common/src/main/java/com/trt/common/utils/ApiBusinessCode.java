package com.trt.common.utils;

public enum ApiBusinessCode {

    SUCCESS(200),
    /**
     * 自定义错误消息
     */
    CUSTOM_MSG(10000),
    /**
     * api 不存在
     */
    API_NOT_FOUND(10001),
    /**
     * 参数错误
     */
    PARAM_INVALID(10002),
    /**
     * 无操作权限
     */
    ACCESSED(10003),
    /**
     * 服务负载高，请重试
     */
    RETRY(10004),
    /**
     * 服务内部出错
     */
    INTERNAL_ERROR(10005),
    /**
     * 目标APP不存在
     */
    TARGET_APP_NOT_EXIST(10006),
    /**
     * 目标PLATFORM不存在
     */
    TARGET_PLATFORM_NOT_EXIST(10007),
    /**
     * 目标METRIC不存在
     */
    TARGET_METRIC_NOT_EXIST(10008),
    /**
     * 目标资源不存在
     */
    TARGET_RESOURCES_NOT_EXIST(10009),
    /**
     * 超时
     */
    TIMEOUT(10010),
    /**
     * 业务错误
     */
    BUSINESS_ERROR(10100),
    /**
     * 数据错误
     */
    DATA_ERROR(10200),
    ;

    public static final String CODE_HEADER = "X-ERROR-CODE";

    private final int code;

    ApiBusinessCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
