package com.zephyr.gateway.util;


import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashSet;

/**
 * 请求工具类
 *
 * @author Zephyr
 * @since 2025-09-18
 */
public class RequestUtils {

    /**
     * 获取原始url
     *
     * @param exchange
     * @return
     */
    public static String getOriginalRequestUrl(ServerWebExchange exchange) {
        LinkedHashSet<URI> uris = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        if (uris != null && !uris.isEmpty()) {
            return String.valueOf(uris.iterator().next());
        }
        return String.valueOf(exchange.getRequest().getURI());
    }
}