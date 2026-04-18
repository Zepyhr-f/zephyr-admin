package com.zephyr.generator.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置信息
 *
 * @author Zephyr
 * @since 2025-09-19
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "generator")
public class GeneratorConfig {
    private Datasource datasource = new Datasource();
    private Strategy strategy = new Strategy();
    private Output output = new Output();

    @Data
    public static class Datasource {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }

    @Data
    public static class Strategy {
        private String prefix;
        private String author;
    }

    @Data
    public static class Output {
        private String baseDir;
        private String packageName;
    }
}