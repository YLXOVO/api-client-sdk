package com.ylx.apiclientsdk.model.request;
import com.ylx.apiclientsdk.model.response.ResultResponse;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class CurrencyRequest extends BaseRequest<CurrencyRequest, ResultResponse> {
    private String method;
    private String path;
    @Override
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public Class<ResultResponse> getResponseClass() {
        return ResultResponse.class;
    }
}
