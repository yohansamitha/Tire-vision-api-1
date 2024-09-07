package com.tire_vision_api_1.utils.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@Component
@ConfigurationProperties("security")
public class SecurityConfigurationProperties {
    private boolean allowedCredentials;
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private List<String> exposedHeaders;
}
