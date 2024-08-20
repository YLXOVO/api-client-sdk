package com.ylx.apiclientsdk.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ylx.apiclientsdk.client.ApiClient;
import com.ylx.apiclientsdk.exception.ApiException;
import com.ylx.apiclientsdk.exception.ErrorCode;
import com.ylx.apiclientsdk.exception.ErrorResponse;
import com.ylx.apiclientsdk.model.request.BaseRequest;
import com.ylx.apiclientsdk.model.response.ResultResponse;
import com.ylx.apiclientsdk.util.SignUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public abstract class BaseService implements ApiService {
    // ApiClient类用于处理与API的交互，包括但不限于发送请求和接收响应
    private ApiClient apiClient;

    // gatewayHost存储API网关的主机地址，用于构建请求URL
    private String gatewayHost;

    /**
     * 检查配置
     *
     * 本方法用于检查并设置API客户端配置，确保访问密钥（AccessKey/SecretKey）已正确配置
     * 如果提供了非空的ApiClient对象且其访问密钥不为空，则设置该ApiClient对象为当前对象的ApiClient
     * 如果未提供ApiClient对象且当前对象的ApiClient也为null，则抛出异常提示需要配置访问密钥
     *
     * @param apiClient 可能用于设置的ApiClient对象，如果为null，则检查当前对象的ApiClient
     * @throws ApiException 如果没有配置访问密钥，则抛出ApiException异常
     */
    public void checkConfig(ApiClient apiClient) throws ApiException {
        // 检查传入的ApiClient和当前对象的ApiClient是否都为null
        if (apiClient == null && this.getApiClient() == null) {
            // 如果都为null，则抛出异常，提示需要配置访问密钥
            throw new ApiException(ErrorCode.NO_AUTH_ERROR, "请先配置密钥AccessKey/SecretKey");
        }
        // 检查传入的ApiClient是否非null且其访问密钥非空
        if (apiClient != null && !StringUtils.isAnyBlank(apiClient.getAccessKey(), apiClient.getSecretKey())) {
            // 如果条件满足，则设置该ApiClient为当前对象的ApiClient
            this.setApiClient(apiClient);
        }
    }


    /**
     * 执行具体请求操作
     *
     * 此方法负责发起请求，并获取返回的HTTP响应它隐藏了请求的具体实现细节，
     * 通过使用泛型来支持不同类型的请求和响应数据类型在请求执行过程中，
     * 如果发生任何异常，将捕获并封装成ApiException，以便调用者可以更方便地处理错误情况
     *
     * @param request 请求对象，包含了请求的具体内容以及期望的返回类型
     * @return HttpResponse 原生的HTTP响应对象
     * @throws ApiException 如果请求过程中出现错误，封装成ApiException抛出
     */
    private <O, T extends ResultResponse> HttpResponse doRequest(BaseRequest<O, T> request) throws ApiException {
        try (HttpResponse httpResponse = getHttpRequestByRequestMethod(request).execute()) {
            return httpResponse;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 根据请求方法获取HTTP响应
     *
     * 此方法根据提供的请求对象，通过不同的HTTP方法（如GET、POST）构建HttpRequest对象
     * 它处理了请求参数的组装、请求头的添加以及请求体的设置
     *
     * @param request 请求对象，包含请求方法、路径等信息
     * @param <T> 泛型参数，表示返回的响应类型，继承自ResultResponse
     * @param <O> 泛型参数，表示请求的参数类型
     * @return 返回构建的HttpRequest对象
     * @throws ApiException 如果请求参数为空、请求方法不存在、请求路径不存在或不支持该请求方法，则抛出ApiException异常
     */
    private <T extends ResultResponse, O> HttpRequest getHttpRequestByRequestMethod(BaseRequest<O, T> request) throws ApiException {
        // 检查请求参数是否为空
        if (ObjectUtils.isEmpty(request)) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "请求参数不能为空");
        }
        // 获取并处理请求路径
        String path = request.getPath().trim();
        // 获取并处理请求方法，确保全大写
        String method = request.getMethod().trim().toUpperCase();

        // 检查请求方法是否存在
        if (ObjectUtils.isEmpty(method)) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "请求方法不存在");
        }
        // 检查请求路径是否存在
        if (ObjectUtils.isEmpty(path)) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, "请求路径不存在");
        }
        // 如果路径以网关主机开头，则移除该部分
        if (path.startsWith(gatewayHost)) {
            path = path.substring(gatewayHost.length());
        }
        // 记录请求日志
        log.info("请求路径:{}", path, "请求方法:{}", method, "请求参数:{}", request.getRequestParams());
        HttpRequest httpRequest;
        // 根据请求方法选择不同的处理方式
        switch (method) {
            case "GET":
                // 构建GET请求
                httpRequest = HttpRequest.get(splicingGetRequest(request, path));
                break;
            case "POST":
                // 构建POST请求
                httpRequest = HttpRequest.post(gatewayHost + path);
                break;
            default:
                // 如果不是支持的请求方法，则抛出异常
                throw new ApiException(ErrorCode.OPERATION_ERROR, "不支持该请求");
        }
        // 添加请求头，并设置请求体
        return httpRequest.addHeaders(getHeaders(JSONUtil.toJsonStr(request), apiClient))
                .body(JSONUtil.toJsonStr(request.getRequestParams()));
    }

    /**
     * 获取请求头
     *
     * 根据传入的请求体和API客户端信息，生成带有认证和签名的请求头
     * 这些请求头用于后续的API请求，以确保请求的合法性和完整性
     *
     * @param body 请求体内容，将被用于生成MD5摘要和签名
     * @param apiClient 包含访问密钥和私密密钥的API客户端信息
     * @return 返回包含请求头字段的Map，包括访问密钥、MD5摘要后的请求体、签名和时间戳
     */
    private Map<String, String> getHeaders(String body, ApiClient apiClient) {
        // 初始化请求头Map
        Map<String, String> hashMap = new HashMap<>(4);
        // 放入访问密钥
        hashMap.put("accessKey", apiClient.getAccessKey());
        // 对请求体进行MD5摘要
        String encodeBody = SecureUtil.md5(body);
        // 放入摘要后的请求体
        hashMap.put("body", encodeBody);
        // 生成请求签名并放入Map
        hashMap.put("sign", SignUtils.genSign(encodeBody, apiClient.getSecretKey()));
        // 生成时间戳并放入Map，用于请求的时间验证
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        // 返回构造好的请求头Map
        return hashMap;
    }


    /**
     * 拼接Get请求
     *
     * @param request 请求对象，包含请求参数等信息
     * @param path    请求路径，通常以/开头
     * @param <T>     泛型参数，表示返回的响应体类型，继承自ResultResponse
     * @param <O>     泛型参数，表示请求参数的类型
     * @return 拼接完成的Get请求URL
     */
    private <T extends ResultResponse, O> String splicingGetRequest(BaseRequest<O, T> request, String path) {
        // 初始化URL拼接的StringBuilder，从网关主机开始
        StringBuilder urlBuilder = new StringBuilder(gatewayHost);
        // 如果urlBuilder以/结尾，且path以/开头，则去掉urlBuilder的最后一个/
        if (urlBuilder.toString().endsWith("/") && path.startsWith("/")) {
            urlBuilder.setLength(urlBuilder.length() - 1);
        }
        // 将路径拼接到URL中
        urlBuilder.append(path);
        // 如果请求参数不为空，开始拼接查询字符串
        if (!request.getRequestParams().isEmpty()) {
            // 以?开始查询字符串
            urlBuilder.append("?");
            // 遍历请求参数，以键值对的形式拼接到URL中
            for (Map.Entry<String, Object> entry : request.getRequestParams().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                urlBuilder.append(key).append("=").append(value).append("&");
            }
            // 删除最后一个&
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        // 记录日志，输出拼接完成的URL
        log.info("GET请求路径：{}", urlBuilder);
        // 返回拼接完成的URL
        return urlBuilder.toString();
    }


    /**
     * 获取响应数据
     *
     * 此方法用于处理API请求并返回相应的响应数据它首先检查是否已经配置了API客户端的访问密钥，
     * 如果没有配置，则抛出异常然后尝试实例化响应类，并通过HTTP请求获取响应数据如果响应状态
     * 不是200（OK），则解析错误响应并将其相关信息放入返回对象中；如果响应状态是200，则尝试
     * 将响应体解析为Json对象，并将其放入返回对象中
     *
     * @param request 请求对象，包含请求的所有必要信息以及如何解析响应
     * @param <O> 请求的输出类型参数
     * @param <T> 响应的类型参数，继承自ResultResponse
     * @return 响应对象，包含从API请求中获取的数据
     * @throws ApiException 如果发生API相关的异常，如未配置密钥或响应解析错误
     */
    public <O, T extends ResultResponse> T res(BaseRequest<O, T> request) throws ApiException {
        // 检查API客户端是否已初始化及其密钥是否已配置
        if (apiClient == null || StringUtils.isAnyBlank(apiClient.getAccessKey(), apiClient.getSecretKey())) {
            throw new ApiException(ErrorCode.NO_AUTH_ERROR, "请先配置密钥AccessKey/SecretKey");
        }

        T rsp;
        try {
            // 获取响应类并实例化
            Class<T> clazz = request.getResponseClass();
            rsp = clazz.newInstance();
        } catch (Exception e) {
            // 如果响应类实例化失败，抛出异常
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }

        // 执行HTTP请求
        HttpResponse httpResponse = doRequest(request);
        // 获取响应体
        String body = httpResponse.body();
        // 初始化数据容器
        Map<String, Object> data = new HashMap<>();

        // 检查HTTP响应状态
        if (httpResponse.getStatus() != 200) {
            // 如果状态不是200，解析错误响应
            ErrorResponse errorResponse = JSONUtil.toBean(body, ErrorResponse.class);
            data.put("errorMessage", errorResponse.getMessage());
            data.put("code", errorResponse.getCode());
        } else {
            try {
                // 尝试解析为Json对象
                data = new Gson().fromJson(body, new TypeToken<Map<String, Object>>() {
                }.getType());
            } catch (Exception e) {
                // 如果解析失败，抛出异常
                throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
            }
        }

        // 将解析后的数据放入响应对象
        rsp.setData(data);
        return rsp;
    }


    /**
     * 执行请求并返回结果
     * 该方法是对ApiService接口中定义的request方法的实现，它负责处理请求并返回相应的结果响应
     * 如果在处理请求时发生异常，将抛出ApiException异常
     *
     * @param request 请求对象，包含请求所需的所有信息
     * @param <O> 请求的输出类型参数
     * @param <T> 请求的返回类型参数，必须是ResultResponse的子类
     * @return 处理请求后的响应结果
     * @throws ApiException 如果在处理请求时发生错误，将抛出此异常
     */
    @Override
    public <O, T extends ResultResponse> T request(BaseRequest<O, T> request) throws ApiException {
        try {
            return res(request);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 使用指定的ApiClient执行请求
     * 该方法在调用前会检查ApiClient的配置是否正确，确保请求能够正常发送
     * 它是对ApiService接口中定义的requestWithClient方法的实现
     *
     * @param apiClient ApiClient对象，用于发送请求
     * @param request 请求对象，包含请求所需的所有信息
     * @param <O> 请求的输出类型参数
     * @param <T> 请求的返回类型参数，必须是ResultResponse的子类
     * @return 处理请求后的响应结果
     * @throws ApiException 如果ApiClient配置错误或在处理请求时发生错误，将抛出此异常
     */
    @Override
    public <O, T extends ResultResponse> T requestWithClient(ApiClient apiClient, BaseRequest<O, T> request) throws ApiException {
        checkConfig(apiClient);
        return request(request);
    }
}
