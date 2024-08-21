package com.ylx.apiclientsdk.service.impl;

import com.ylx.apiclientsdk.client.ApiClient;
import com.ylx.apiclientsdk.exception.ApiException;
import com.ylx.apiclientsdk.model.request.IpInfoRequest;
import com.ylx.apiclientsdk.model.response.ResultResponse;
import com.ylx.apiclientsdk.service.ApiService;
import com.ylx.apiclientsdk.service.BaseService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiServiceImpl extends BaseService implements ApiService {

    /**
     * 使用ApiClient获取IP信息
     * 此方法允许通过ApiClient自定义请求方式，以支持不同的API调用策略
     *
     * @param apiClient ApiClient实例，用于发送API请求
     * @param request 包含IP信息请求参数的对象
     * @return ResultResponse 包含请求结果的对象，调用requestWithClient方法发送请求并返回结果
     * @throws ApiException 如果请求处理过程中发生错误，则抛出ApiException异常
     */
    @Override
    public ResultResponse getIpInfoWithClient(ApiClient apiClient, IpInfoRequest request) throws ApiException {
        // 调用自定义客户端方法发送请求，并返回结果
        return requestWithClient(apiClient, request);
    }

    /**
     * 默认方式获取IP信息
     * 此方法使用类内部默认的请求方式，适用于大多数情况
     *
     * @param request 包含IP信息请求参数的对象
     * @return ResultResponse 包含请求结果的对象，调用request方法发送请求并返回结果
     * @throws ApiException 如果请求处理过程中发生错误，则抛出ApiException异常
     */
    @Override
    public ResultResponse getIpInfo(IpInfoRequest request) throws ApiException {
        // 使用默认方式发送请求，并返回结果
        return request(request);
    }
}

