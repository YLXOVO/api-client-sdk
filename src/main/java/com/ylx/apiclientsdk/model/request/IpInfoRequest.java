package com.ylx.apiclientsdk.model.request;

import com.ylx.apiclientsdk.model.enums.RequestMethodEnum;
import com.ylx.apiclientsdk.model.response.ResultResponse;

public class IpInfoRequest extends BaseRequest<IpInfoRequest , ResultResponse>{
    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();
    }

    @Override
    public String getPath() {
        return "/ipInfo";
    }

    @Override
    public Class<ResultResponse> getResponseClass() {
        return ResultResponse.class;
    }
}
