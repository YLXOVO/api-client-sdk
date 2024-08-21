package com.ylx.apiclientsdk.service;

import com.ylx.apiclientsdk.client.ApiClient;
import com.ylx.apiclientsdk.exception.ApiException;
import com.ylx.apiclientsdk.model.request.BaseRequest;
import com.ylx.apiclientsdk.model.request.IpInfoRequest;
import com.ylx.apiclientsdk.model.response.ResultResponse;

/**
 * 定义了API服务的基本行为，提供了一种通用的方法来发送请求并处理响应。
 * 该接口的目的是确保所有API交互都遵循统一的模式，以便于错误处理、日志记录和安全性管理。
 * @author ylx
 * @date 2023/9/6 15:05
 */
public interface ApiService {
    /**
     * 使用默认的客户端发送一个请求，并返回处理后的结果.
     *
     * @param request 请求对象，包含了发送请求所需的所有信息。
     * @param <O>     请求体的类型参数，由具体实现决定。
     * @param <T>     响应体的类型参数，必须是ResultResponse的子类或实现。
     * @return 处理后的响应结果。
     * @throws ApiException 如果请求处理过程中发生了异常。
     */
    <O,T extends ResultResponse> T request(BaseRequest<O, T> request) throws ApiException;

    /**
     * 使用指定的客户端发送一个请求，并返回处理后的结果.
     * 该方法允许传入自定义的ApiClient实例，从而提供更灵活的请求发送方式。
     *
     * @param apiClient 客户端实例，用于发送请求。
     * @param request   请求对象，包含了发送请求所需的所有信息。
     * @param <O>       请求体的类型参数，由具体实现决定。
     * @param <T>       响应体的类型参数，必须是ResultResponse的子类或实现。
     * @return 处理后的响应结果。
     * @throws ApiException 如果请求处理过程中发生了异常。
     */
    <O,T extends ResultResponse> T requestWithClient(ApiClient apiClient, BaseRequest<O, T> request) throws ApiException;

    /**
     * 通过指定的ApiClient获取IP信息
     * 此方法允许通过特定的ApiClient实例发送IpInfoRequest，以获取IP信息
     * 主要用于 situations where a specific apiClient instance needs to be used
     *
     * @param apiClient ApiClient实例，用于发送API请求
     * @param request IpInfoRequest对象，包含获取IP信息所需的参数
     * @return ResultResponse对象，包含IP信息查询结果
     * @throws ApiException 如果API调用出现错误
     */
    ResultResponse getIpInfoWithClient(ApiClient apiClient, IpInfoRequest request) throws ApiException;


    /**
     * 直接通过默认ApiClient获取IP信息
     * 此方法使用默认的ApiClient实例发送IpInfoRequest，适用于大多数情况
     * 主要用于 situations where the default apiClient can be used
     *
     * @param request IpInfoRequest对象，包含获取IP信息所需的参数
     * @return ResultResponse对象，包含IP信息查询结果
     * @throws ApiException 如果API调用出现错误
     */
    ResultResponse getIpInfo(IpInfoRequest request) throws ApiException;
}

