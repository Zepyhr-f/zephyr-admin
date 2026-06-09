package com.zephyr.core.boot.web;

import com.zephyr.redis.Constant.RedisConstant;
import com.zephyr.redis.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;

import static com.zephyr.core.tool.constant.WebConstants.*;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {
    private static final String ANONYMOUS_USER_CODE = "-1";
    private static final long TIMESTAMP_TOLERANCE_SECONDS = 300; // 5分钟防重放

    @Value("${zephyr.gateway.secret:zephyr-default-secret}")
    private String gatewaySecret;

    @Autowired(required = false)
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userCode = request.getHeader(USER_CODE_HEADER);
        String tenantCode = request.getHeader(TENANT_CODE_HEADER);
        String timestamp = request.getHeader(TIMESTAMP_HEADER);
        String sign = request.getHeader(GATEWAY_SIGN_HEADER);

        // URL 解码（网关做了编码）
        if (userCode != null) {
            userCode = URLDecoder.decode(userCode, StandardCharsets.UTF_8);
        }
        if (tenantCode != null) {
            tenantCode = URLDecoder.decode(tenantCode, StandardCharsets.UTF_8);
        }
        if (timestamp != null) {
            timestamp = URLDecoder.decode(timestamp, StandardCharsets.UTF_8);
        }
        if (sign != null) {
            sign = URLDecoder.decode(sign, StandardCharsets.UTF_8);
        }

        // 匿名请求放行（白名单接口）
        if (userCode == null || userCode.isEmpty()) {
            UserSession session = new UserSession();
            session.setUserCode(ANONYMOUS_USER_CODE);
            session.setTenantCode(tenantCode);
            UserContextHolder.set(session);
            return true;
        }

        // 验签与防重放已交由 GatewaySecurityInterceptor 和 Gateway 处理，此处直接放行

        // 从 Redis 获取 UserSession
        UserSession session = null;
        if (redisUtil != null) {
            session = redisUtil.getObject(RedisConstant.USER_INFO_PREFIX + tenantCode + ":" + userCode, UserSession.class);
        }

        if (session == null) {
            // 直接从 Header 构建用户会话 (Fallback)
            session = buildUserSessionFromHeaders(userCode, tenantCode);
        }
        
        UserContextHolder.set(session);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        // 请求结束必须清理，防止内存泄漏和线程池复用污染
        UserContextHolder.clear();
    }



    private UserSession buildUserSessionFromHeaders(String userCode, String tenantCode) {
        UserSession session = new UserSession();
        session.setUserCode(userCode);
        session.setTenantCode(tenantCode);

        return session;
    }

    private List<String> parseListHeader(String headerValue) {
        if (headerValue == null || headerValue.isEmpty()) {
            return List.of();
        }
        try {
            String decoded = URLDecoder.decode(headerValue, StandardCharsets.UTF_8);
            if (decoded.isEmpty()) {
                return List.of();
            }
            return List.of(decoded.split(","));
        } catch (Exception e) {
            log.warn("解析Header失败: {}", headerValue, e);
            return List.of();
        }
    }
}
