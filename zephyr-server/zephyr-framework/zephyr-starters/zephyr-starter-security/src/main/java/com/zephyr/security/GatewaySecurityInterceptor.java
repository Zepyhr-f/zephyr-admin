package com.zephyr.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class GatewaySecurityInterceptor implements HandlerInterceptor {

    private final GatewaySignUtil gatewaySignUtil;
    private final ObjectMapper objectMapper;

    // 允许的时间偏差：5分钟
    private static final long TIME_SKEW_MILLIS = 5 * 60 * 1000L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String timestamp = request.getHeader(SecurityConstants.TIMESTAMP_HEADER);
        String nonce = request.getHeader(SecurityConstants.NONCE_HEADER);
        String signature = request.getHeader(SecurityConstants.GATEWAY_SIGN_HEADER);
        String tenantCode = request.getHeader(SecurityConstants.TENANT_CODE_HEADER);
        String userCode = request.getHeader(SecurityConstants.USER_CODE_HEADER);
        String requestId = request.getHeader(SecurityConstants.REQUEST_ID_HEADER);
        String rolesStr = request.getHeader(SecurityConstants.ROLES_HEADER);

        // 如果完全没有签名头，说明可能是不经过网关直接访问，拦截
        if (!StringUtils.hasText(signature) || !StringUtils.hasText(timestamp) || !StringUtils.hasText(nonce)) {
            log.warn("Missing security headers. RequestId: {}", requestId);
            return respondError(response, 401, "Missing Security Headers");
        }

        // 校验时间戳防重放
        try {
            long ts = Long.parseLong(timestamp);
            if (Math.abs(System.currentTimeMillis() - ts) > TIME_SKEW_MILLIS) {
                log.warn("Request timestamp expired or skewed. RequestId: {}", requestId);
                return respondError(response, 401, "Request Expired");
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid timestamp format. RequestId: {}", requestId);
            return respondError(response, 401, "Invalid Timestamp");
        }

        String method = request.getMethod();
        String path = request.getRequestURI();

        // 验签
        String expectedSign = gatewaySignUtil.generateSign(
                timestamp,
                nonce,
                method,
                path,
                tenantCode == null ? "" : tenantCode,
                userCode == null ? "" : userCode,
                requestId == null ? "" : requestId
        );

        if (!expectedSign.equals(signature)) {
            log.warn("Gateway signature verification failed. RequestId: {}, Path: {}", requestId, path);
            return respondError(response, 403, "Invalid Signature");
        }

        // 验签通过，组装并注入上下文
        UserContext context = UserContext.builder()
                .userCode(userCode)
                .tenantCode(tenantCode)
                .requestId(requestId)
                .roles(StringUtils.hasText(rolesStr) ? Arrays.asList(rolesStr.split(",")) : Collections.emptyList())
                .build();
        UserContextHolder.setContext(context);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求处理完毕，清理 ThreadLocal 避免内存泄漏
        UserContextHolder.clearContext();
    }

    private boolean respondError(HttpServletResponse response, int status, String msg) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> res = new HashMap<>();
        res.put("code", status);
        res.put("msg", msg);
        res.put("success", false);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(objectMapper.writeValueAsString(res));
            writer.flush();
        }
        return false;
    }
}
