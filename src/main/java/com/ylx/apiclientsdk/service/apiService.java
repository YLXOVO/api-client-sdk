package com.ylx.apiclientsdk.service;

import com.ylx.apiclientsdk.exception.ApiException;
import com.ylx.apiclientsdk.model.request.BaseRequest;
import com.ylx.apiclientsdk.model.response.ResultResponse;

/**
 * @author Ylx
 * @version 1.0
 * @description TODO
 * @date 2023/9/6 15:05
 */
public interface apiService {
    <O,T extends ResultResponse> T request(BaseRequest<O, T> request) throws ApiException;
}
