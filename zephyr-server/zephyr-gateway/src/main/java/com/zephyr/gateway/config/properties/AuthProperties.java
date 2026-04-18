package com.zephyr.gateway.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-09-18
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "zephyr.auth")
public class AuthProperties {
    /**
     * 白名单API
     */
    private List<String> whiteApi = new ArrayList<>();
}