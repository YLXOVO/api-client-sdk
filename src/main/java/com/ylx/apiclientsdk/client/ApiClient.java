package com.ylx.apiclientsdk.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 调用第三方接口的客户端
 *
 * @create 2023-09-06 15:04
 * @author Ylx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiClient {

    private String accessKey;
    private String secretKey;

}
