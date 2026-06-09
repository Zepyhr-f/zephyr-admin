package com.zephyr.system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Zephyr System API")
                        .version("1.0")
                        .description("Zephyr 系统管理服务 API 文档")
                        .contact(new Contact().name("Zephyr").email("admin@zephyr.com")));
    }
}
