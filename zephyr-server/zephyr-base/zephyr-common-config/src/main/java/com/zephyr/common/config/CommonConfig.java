package com.zephyr.common.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "common")
public class CommonConfig {
    private String nacosServerAddr;
    private String nacosNamespace;
}