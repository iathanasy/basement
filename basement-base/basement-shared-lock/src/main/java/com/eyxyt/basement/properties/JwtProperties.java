package com.eyxyt.basement.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author cd.wang
 * @create 2020-07-15 16:26
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "jwt.shared-lock")
@Component
public class JwtProperties {
    private boolean open;
    private String secret;
    private Long expire;
    private Long refreshExpire;
    private String headerKey;
    private String tokenPrefix;
}
