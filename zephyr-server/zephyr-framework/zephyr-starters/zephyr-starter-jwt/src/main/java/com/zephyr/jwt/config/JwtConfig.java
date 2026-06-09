package com.zephyr.jwt.config;


import com.zephyr.jwt.provider.JwtKeyProvider;
import com.zephyr.jwt.provider.JwtProviderFactory;
import com.zephyr.jwt.util.JwtUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT Configuration
 *
 * @author Zephyr
 * @since 2025-09-13
 */
@Configuration
public class JwtConfig {
    @Value("${jwt.algorithm:HS256}")
    private String algorithm; // 默认使用 HS256 算法

    @Bean
    public JwtUtil jwtUtil(JwtProviderFactory factory, @Value("${jwt.expiration:3600000}") Long expiration) {
        JwtKeyProvider provider = factory.getProvider(algorithm); // 启动时只选一次
        return new JwtUtil(provider, SignatureAlgorithm.valueOf(algorithm), expiration);
    }
}