package com.zephyr.gateway.filter;

import com.zephyr.security.SecurityConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class HeaderSanitizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    httpHeaders.remove(SecurityConstants.USER_CODE_HEADER);
                    httpHeaders.remove(SecurityConstants.TENANT_CODE_HEADER);
                    httpHeaders.remove(SecurityConstants.ROLES_HEADER);
                    httpHeaders.remove(SecurityConstants.TIMESTAMP_HEADER);
                    httpHeaders.remove(SecurityConstants.NONCE_HEADER);
                    httpHeaders.remove(SecurityConstants.GATEWAY_SIGN_HEADER);
                })
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
