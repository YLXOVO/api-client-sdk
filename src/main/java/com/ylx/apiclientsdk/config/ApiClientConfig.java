package com.ylx.apiclientsdk.config;

import com.ylx.apiclientsdk.client.ApiClient;
import com.ylx.apiclientsdk.service.ApiService;
import com.ylx.apiclientsdk.service.impl.ApiServiceImpl;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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
    /**
     * 网关
     */
    private String host;
    @Bean
    public ApiClient apiClient() {
        return new ApiClient(accessKey, secretKey);
    }

    @Bean
    public ApiService apiService() {
        ApiServiceImpl apiService = new ApiServiceImpl();
        apiService.setApiClient(new ApiClient(accessKey, secretKey));
        if (StringUtils.isNotBlank(host)){
            apiService.setGatewayHost(host);
        }
        return apiService;
    }
}
