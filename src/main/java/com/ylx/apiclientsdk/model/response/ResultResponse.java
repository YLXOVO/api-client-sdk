package com.ylx.apiclientsdk.model.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
/**
 * ResultResponse类用于封装API调用的结果响应
 * 它主要包含一个可动态添加属性的Map对象，用于存放响应数据
 */
@Data // Lombok注解，自动生成getter, setter, equals, hashCode, toString等方法
@NoArgsConstructor // Lombok注解，自动生成无参构造方法
public class ResultResponse {
    // 序列化版本号，用于保证序列化和反序列化过程的兼容性
    private static final long serialVersionUID = 1L;

    // 存储响应数据的Map对象，键为String类型，值为Object类型，可以在响应中动态添加任何属性
    private Map<String ,Object> data = new HashMap<>();

    /**
     * 获取响应数据的Map对象
     *
     * @return 包含所有响应数据的Map对象
     */
    @JsonAnyGetter // Jackson注解，用于动态序列化任意属性
    public Map<String,Object > getData (){
        return data;
    }

    /**
     * 设置响应数据的Map对象
     *
     * @param data 包含响应数据的Map对象
     */
    public void setData(Map<String ,Object> data){
        this.data = data;
    }
}

