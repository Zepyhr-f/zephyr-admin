package com.zephyr.gateway.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zephyr.gateway.config.properties.DefaultSkipProp;
import com.zephyr.gateway.config.properties.AuthProperties;
import com.zephyr.gateway.util.RequestUtils;
import com.zephyr.jwt.util.JwtUtil;
import com.zephyr.redis.Constant.RedisConstant;
import com.zephyr.redis.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.zephyr.core.tool.constant.WebConstants.*;
import static com.zephyr.jwt.config.JwtConstant.*;

/**
 * 认证过滤器
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Value("${zephyr.gateway.secret:zephyr-default-secret}")
    private String gatewaySecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();

        // 路径校验
        String originalRequestUrl = RequestUtils.getOriginalRequestUrl(exchange);
        String path = exchange.getRequest().getURI().getPath();
        if (this.isSkip(path) || this.isSkip(originalRequestUrl)) {
            return chain.filter(exchange);
        }

        // token校验
        String authHeader = exchange.getRequest().getHeaders().getFirst(HEADER_STRING);
        if (authHeader == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return unAuth(response, "Token缺失");
        }
        String token = authHeader.startsWith(TOKEN_PREFIX) ?
                authHeader.substring(TOKEN_PREFIX_LENGTH) : authHeader;

        Claims claims;
        try {
            claims = jwtUtil.extractAllClaims(token);
        } catch (JwtException e) {
            log.error("JWT解析异常: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return unAuth(response, "Token格式非法或已过期");
        }

        // 校验 jti 黑名单（可选）
        String jti = claims.get(JTI, String.class);
        if (jti != null) {
            String blacklisted = redisUtil.getString(RedisConstant.BLACKLIST_PREFIX + jti);
            if (blacklisted != null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return unAuth(response, "Token已被注销");
            }
        }

        // 校验过期（extractAllClaims 已经校验签名，但显式再校验一次 exp 更安全）
        if (jwtUtil.isTokenExpired(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return unAuth(response, "Token已过期");
        }

        String userCode = claims.getSubject();
        String tenantCode = claims.get(TENANT_CODE, String.class);

        // 生成签名
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = generateGatewaySign(userCode, tenantCode, timestamp);

        // 透传用户信息（新规范）
        ServerHttpRequest.Builder mutate = exchange.getRequest().mutate();
        addHeader(mutate, USER_CODE_HEADER, userCode);
        addHeader(mutate, TENANT_CODE_HEADER, tenantCode);
        addHeader(mutate, TIMESTAMP_HEADER, timestamp);
        addHeader(mutate, GATEWAY_SIGN_HEADER, sign);

        return chain.filter(exchange);
    }

    private boolean isSkip(String path) {
        return DefaultSkipProp.getDefaultSkipUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path))
                || authProperties.getWhiteApi().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String result = "";
        try {
            result = objectMapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = URLEncoder.encode(valueStr, StandardCharsets.UTF_8);
        mutate.header(name, valueEncode);
    }

    private String generateGatewaySign(String userCode, String tenantCode, String timestamp) {
        try {
            String data = String.valueOf(userCode) + String.valueOf(tenantCode) + timestamp;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(gatewaySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signBytes);
        } catch (Exception e) {
            log.error("网关签名生成失败", e);
            throw new RuntimeException("网关签名生成失败", e);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
