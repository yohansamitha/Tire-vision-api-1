package com.tire_vision_api_1.utils.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@Component
@ConfigurationProperties("application.jwt")
public class JWTConfig {
    private String secretKey;
    private String tokenPrefix;
    private Long tokenExp;
}
