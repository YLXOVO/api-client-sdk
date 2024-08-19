package com.ylx.apiclientsdk.config;

import com.ylx.apiclientsdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("api.client")
@Data
@ComponentScan
public class ApiClientConfig {
    private String accessKey;
    private String secretKey;
    @Bean
    public ApiClient apiClient() {
        return new ApiClient(accessKey, secretKey);
    }
    // TODO: 添加配置类其他配置，比如apiService的初始化Bean
}
