package com.wjz.webserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "user.jwt")
@Data
public class JwtConfig {
    private String secretKey;
    private long ttl;
    private String tokenName;
}