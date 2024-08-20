package com.ylx.apiclientsdk.exception;

import lombok.Data;

/**
 * @Author: ylx
 * @Date: 2024/8/20 15:44
 * @Description:
 */
@Data
public class ErrorResponse {
    private String message;
    private int code;
}