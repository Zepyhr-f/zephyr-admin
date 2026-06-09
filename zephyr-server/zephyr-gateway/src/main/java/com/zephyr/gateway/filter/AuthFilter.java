package com.zephyr.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zephyr.gateway.config.AuthProperties;
import com.zephyr.gateway.constant.GatewayConstant;
import com.zephyr.security.SecurityConstants;
import com.zephyr.gateway.rbac.RbacService;
import com.zephyr.security.GatewaySignUtil;
import com.zephyr.gateway.security.NonceStore;
import com.zephyr.jwt.util.JwtUtil;
import com.zephyr.redis.Constant.RedisConstant;
import com.zephyr.redis.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static com.zephyr.jwt.config.JwtConstant.HEADER_STRING;
import static com.zephyr.jwt.config.JwtConstant.TOKEN_PREFIX;
import static com.zephyr.jwt.config.JwtConstant.TOKEN_PREFIX_LENGTH;
import static com.zephyr.jwt.config.JwtConstant.JTI;
import static com.zephyr.jwt.config.JwtConstant.TENANT_CODE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final RbacService rbacService;
    private final GatewaySignUtil signUtil;
    private final NonceStore nonceStore;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        // 1. 白名单放行
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // 2. Token 校验
        String authHeader = exchange.getRequest().getHeaders().getFirst(HEADER_STRING);
        if (authHeader == null) {
            return unAuth(response, "未授权，Token缺失");
        }
        
        String token = authHeader.startsWith(TOKEN_PREFIX) ? authHeader.substring(TOKEN_PREFIX_LENGTH) : authHeader;

        Claims claims;
        try {
            claims = jwtUtil.extractAllClaims(token);
        } catch (JwtException e) {
            log.error("JWT解析异常: {}", e.getMessage());
            return unAuth(response, "Token格式非法或已过期");
        }

        if (jwtUtil.isTokenExpired(token)) {
            return unAuth(response, "Token已过期");
        }

        String jti = claims.get(JTI, String.class);
        if (jti != null) {
            if (redisUtil.getString(RedisConstant.BLACKLIST_PREFIX + jti) != null) {
                return unAuth(response, "Token已被注销");
            }
        }

        String userCode = claims.getSubject();
        String tenantCode = claims.get(TENANT_CODE, String.class);

        // 3. 基础认证与严格鉴权
        Set<String> roles = rbacService.getUserRoles(tenantCode, userCode);
        if (!isBasic(path)) {
            // RBAC strict
            if (!rbacService.checkPermission(tenantCode, method, path, roles)) {
                return forbidden(response, "权限不足，禁止访问");
            }
        }

        // 4. 上下文透传与网关签名
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = nonceStore.generateNonce();
        String requestId = exchange.getRequest().getHeaders().getFirst(GatewayConstant.X_REQUEST_ID);
        if(requestId == null) requestId = "";

        String sign = signUtil.generateSign(timestamp, nonce, method, path, tenantCode, userCode, requestId);

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(SecurityConstants.USER_CODE_HEADER, userCode)
                .header(SecurityConstants.TENANT_CODE_HEADER, tenantCode)
                .header(SecurityConstants.ROLES_HEADER, String.join(",", roles))
                .header(SecurityConstants.TIMESTAMP_HEADER, timestamp)
                .header(SecurityConstants.NONCE_HEADER, nonce)
                .header(SecurityConstants.GATEWAY_SIGN_HEADER, sign)
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean isWhiteList(String path) {
        return authProperties.getWhiteApi().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private boolean isBasic(String path) {
        if (authProperties.getBasicApi() == null) return false;
        return authProperties.getBasicApi().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
        return jsonResponse(resp, HttpStatus.UNAUTHORIZED, msg);
    }
    
    private Mono<Void> forbidden(ServerHttpResponse resp, String msg) {
        return jsonResponse(resp, HttpStatus.FORBIDDEN, msg);
    }

    private Mono<Void> jsonResponse(ServerHttpResponse resp, HttpStatus status, String msg) {
        resp.setStatusCode(status);
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

    @Override
    public int getOrder() {
        return 0; // After HeaderSanitizeFilter
    }
}
