package com.ylx.apiclientsdk.model.enums;

/**
 * 请求方法枚举类
 * @author ylx
 */
public enum RequestMethodEnum {
    GET("GET","GET"),
    POST("POST","POST");
    private final String text;
    private final String value;
    RequestMethodEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
