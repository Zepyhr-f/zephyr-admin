package com.zephyr.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.zephyr.security")
public class SecurityAutoConfiguration {

    // 仅在 Servlet (MVC) 模式下，注入拦截器
    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @RequiredArgsConstructor
    public static class MvcSecurityConfig implements WebMvcConfigurer {

        private final GatewaySignUtil gatewaySignUtil;
        private final ObjectMapper objectMapper;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new GatewaySecurityInterceptor(gatewaySignUtil, objectMapper))
                    .addPathPatterns("/**")
                    // 可以根据需要排除如 swagger 接口等
                    .excludePathPatterns("/v3/api-docs/**", "/swagger-ui/**", "/doc.html", "/webjars/**");
        }
    }
}
