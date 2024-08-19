package com.ylx.apiclientsdk.exception;

/**
 * 错误码枚举类，用于定义API调用过程中可能出现的各种错误状态和错误码
 */
public enum ErrorCode {
    /**
     * 成功
     */
    SUCCESS(0, "ok"),
    /**
     * 请求参数错误
     */
    PARAMS_ERROR(40000, "请求参数错误"),
    /**
     * 未登录
     */
    NOT_LOGIN_ERROR(40100, "未登录"),
    /**
     * 无权限
     */
    NO_AUTH_ERROR(40101, "无权限"),
    /**
     * 请求数据不存在
     */
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    /**
     * 禁止访问
     */
    FORBIDDEN_ERROR(40300, "禁止访问"),
    /**
     * 系统错误
     */
    SYSTEM_ERROR(50000, "系统内部异常"),
    /**
     * 操作错误
     */
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误消息
     */
    private String message;

    /**
     * 构造函数，初始化错误码和错误消息
     *
     * @param code    错误码
     * @param message 错误消息
     */
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取错误消息
     *
     * @return 错误消息
     */
    public String getMessage() {
        return message;
    }
}
