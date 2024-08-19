package com.ylx.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ylx.apiclientsdk.model.User;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import static com.ylx.apiclientsdk.util.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 *
 * @create 2023-09-06 15:04
 * @author Ylx
 */
public class ApiClient {

    private String accessKey;
    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }
    // 使用Get方法从服务器中获取名称信息
    public String getNameByGet(String name){
        // 可以单独传入http参数，这样参数会自动做URL编码，拼接到URL中
        HashMap<String,Object> paramMap = new HashMap<>();
        // 将“name”参数添加到paramMap映射中
        paramMap.put("name",name);
        // 使用HttpUtil工具发起GET请求，并获取服务器返回的结果
        String result = HttpUtil.get("http://localhost:8123/api/name/", paramMap);
        // 打印服务器返回的结果
        System.out.println(result);
        // 返回服务器返回的结果
        return result;
    }
    // 使用POST方法从服务器获取名称信息
    public String getNameByPost(String name){
        // 可以单独传入HTTP参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        // 使用HttpUtil工具发起POST请求，并获取服务器返回的结果
        String result = HttpUtil.post("http://localhost:8123/api/name/post", paramMap);
        System.out.println(result);
        return result;
    }

    /**
     * 根据请求体生成请求头的Map
     *
     * @param body 请求体内容，将被用于计算签名和作为请求的一部分
     * @return 包含了访问密钥、随机数、请求体、时间戳和签名的请求头Map
     *
     * 注意：本方法中生成的签名（sign）是基于传入的secretKey和headerMap中的内容计算得到的
     */
    private Map<String,String> getHeaderMap(String body){
        // 初始化一个空的Map来存储请求头信息
        Map<String ,String> headerMap = new HashMap<>();
        // 存入访问密钥，用于身份验证
        headerMap.put("accessKey",accessKey);
        // 存入一个4位的随机数，用于增加请求的唯一性
        headerMap.put("nonce", RandomUtil.randomNumbers(4));
        // 将请求体内容存入Map，便于后续处理
        headerMap.put("body",body);
        // 存入当前时间的时间戳，用于请求的时间验证
        headerMap.put("timestamp",String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
        // 根据当前body的内容和secretKey计算签名，并存入Map
        headerMap.put("sign",genSign(body,secretKey));
        // 返回构造完成的请求头Map
        return headerMap;
    }


    public String getUserNameByPost(User user){
        // 将User对象转换为JSON字符串
        String json = JSONUtil.toJsonStr(user);
        // 使用HttpRequest工具发起POST请求，并获取服务器的响应
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8123/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        // 打印服务器返回的状态码
        System.out.println(httpResponse.getStatus());
        // 获取服务器返回的结果
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }
}
