package com.ylx.apiclientsdk.model.request;
import com.ylx.apiclientsdk.model.response.ResultResponse;
import lombok.experimental.Accessors;

/**
 * CurrencyRequest 类继承自 BaseRequest，用于构建针对相关的请求
 * 它提供了设置和获取请求方法（HTTP 方法）以及请求路径（API 的具体路径）的功能
 * 并指定了响应类型为 ResultResponse
 */
@Accessors(chain = true) // Lombok 注解，用于简化方法链调用
public class CurrencyRequest extends BaseRequest<Object, ResultResponse> {
    // 请求方法，例如 GET, POST
    private String method;
    // 请求路径，例如 /currency/exchange
    private String path;

    /**
     * 获取本次请求的方法
     * @return 当前设置的请求方法
     */
    @Override
    public String getMethod() {
        return method;
    }

    /**
     * 设置本次请求的方法
     * @param method 请求方法，如 GET, POST
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 获取本次请求的路径
     * @return 当前设置的请求路径
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * 设置本次请求的路径
     * @param path 请求路径，如 /currency/exchange
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取本次请求的响应类型
     * @return 响应类型为 ResultResponse.class
     */
    @Override
    public Class<ResultResponse> getResponseClass() {
        return ResultResponse.class;
    }
}

