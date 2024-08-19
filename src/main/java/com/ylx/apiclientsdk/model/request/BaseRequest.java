package com.ylx.apiclientsdk.model.request;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ylx.apiclientsdk.model.response.ResultResponse;

import java.util.HashMap;
import java.util.Map;


/**
 * 抽象类BaseRequest用于构建和处理请求的基础框架
 * 它定义了请求的基本属性和方法，用于子类具体实现
 * 该类泛型参数O表示请求参数的类型，T表示响应结果的类型，通常为ResultResponse的子类
 *
 * @param <O> 请求参数的类型
 * @param <T> 响应结果的类型，继承自ResultResponse
 */
public abstract class BaseRequest<O,T extends ResultResponse> {
    // 存储请求参数的Map，使用Object作为泛型类型以提高灵活性
    private Map<String,Object> requestParams = new HashMap<>();

    /**
     * 获取请求的方法（如GET，POST等）
     * 该方法由子类具体实现
     *
     * @return 请求的方法
     */
    public abstract String getMethod();

    /**
     * 获取请求的路径
     * 该方法由子类具体实现
     *
     * @return 请求的路径
     */
    public abstract String getPath();

    /**
     * 获取响应对象的类类型
     * 该方法由子类具体实现，用于解析响应数据
     *
     * @return 响应对象的Class类型
     */
    public abstract Class<T> getResponseClass();

    /**
     * 获取请求参数
     *
     * @return 请求参数的Map对象
     */
    public Map<String, Object> getRequestParams(){
        return requestParams;
    }

    /**
     * 设置请求参数
     * 该方法使用Gson库将传入的请求参数对象转换为Map存储
     *
     * @param params 请求参数对象
     */
    public void setRequestParams(O params){
        // 使用Gson将传入的请求参数对象转换为JSON字符串，再转换为Map
        this.requestParams = new Gson().fromJson(JSONUtil.toJsonStr(params), new TypeToken<Map<String,Object>>(){}.getType());
    }
}

