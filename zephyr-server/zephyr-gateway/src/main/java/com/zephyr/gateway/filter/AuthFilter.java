package com.zephyr.gateway.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zephyr.core.tool.api.R;
import com.zephyr.gateway.config.properties.DefaultSkipProp;
import com.zephyr.gateway.config.properties.AuthProperties;
import com.zephyr.gateway.util.RequestUtils;
import com.zephyr.jwt.util.JwtUtil;
import com.zephyr.redis.Constant.RedisConstant;
import com.zephyr.redis.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

//import static com.zephyr.core.tool.constant.WebConstants.*;
import static com.zephyr.jwt.config.JwtConstant.*;

/**
 * 认证过滤器
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

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

        String userKey = RedisConstant.TOKEN_PREFIX + claims.get(USER_NAME);
        String rToken = redisUtil.getString(userKey);
        if (!token.equals(rToken)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return unAuth(response, "Token无效或已过期");
        }

        // 权限校验：网关只做 Token 有效性验证（路径级细粒度权限由后端服务自行控制）
        // 注：rolePermissions 中存储的是 perms 标识（如 sys:user:list），与 URL 路径格式不同，
        //     不在网关层做路径比对，避免误拦截合法请求。
        Object roleIdObj = claims.get(ROLE_ID);
        if (roleIdObj == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return unAuth(response, "Token中缺少角色信息，请重新登录");
        }
        // Token 合法，直接放行（已过认证）

//        // 透传用户信息
//        ServerHttpRequest.Builder mutate = exchange.getRequest().mutate();
//        addHeader(mutate,USER_ID_HEADER, claims.get(USER_ID));
//        addHeader(mutate, USER_NAME_HEADER, claims.get(USER_NAME));
//        addHeader(mutate, ROLE_ID_HEADER, claims.get(ROLE_ID));

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

    @Override
    public int getOrder() {
        return -1;
    }
}