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
    private static final String ANONYMOUS_ROLE_CODE = "-1";
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

        // URL 解码（网关可能做了编码）
        if (userCode != null) {
            userCode = URLDecoder.decode(userCode, StandardCharsets.UTF_8);
        }
        if (tenantCode != null) {
            tenantCode = URLDecoder.decode(tenantCode, StandardCharsets.UTF_8);
        }

        // 匿名请求放行（白名单接口）
        if (userCode == null || userCode.isEmpty()) {
            UserSession session = new UserSession();
            session.setUserCode(ANONYMOUS_USER_CODE);
            session.setTenantCode(tenantCode);
            session.setRoleCodes(new HashSet<>(List.of(ANONYMOUS_ROLE_CODE)));
            UserContextHolder.set(session);
            return true;
        }

        // 校验签名
        if (!verifySign(userCode, tenantCode, timestamp, sign)) {
            log.warn("网关签名校验失败: userCode={}, timestamp={}", userCode, timestamp);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 校验 timestamp（防重放）
        if (!verifyTimestamp(timestamp)) {
            log.warn("请求时间戳校验失败: timestamp={}", timestamp);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 从 Redis 加载用户详情
        UserSession session = loadUserSession(userCode, tenantCode);
        UserContextHolder.set(session);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        // 请求结束必须清理，防止内存泄漏和线程池复用污染
        UserContextHolder.clear();
    }

    private boolean verifySign(String userCode, String tenantCode, String timestamp, String sign) {
        if (sign == null || timestamp == null) {
            return false;
        }
        try {
            String data = String.valueOf(userCode) + String.valueOf(tenantCode) + timestamp;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(gatewaySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] expected = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String expectedSign = Base64.getEncoder().encodeToString(expected);
            return expectedSign.equals(sign);
        } catch (Exception e) {
            log.error("签名校验异常", e);
            return false;
        }
    }

    private boolean verifyTimestamp(String timestamp) {
        try {
            long ts = Long.parseLong(timestamp);
            long now = System.currentTimeMillis() / 1000;
            return Math.abs(now - ts) <= TIMESTAMP_TOLERANCE_SECONDS;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private UserSession loadUserSession(String userCode, String tenantCode) {
        // 尝试从 Redis 缓存加载
        if (redisUtil != null) {
            String cacheKey = RedisConstant.USER_INFO_PREFIX + userCode;
            UserSession cached = redisUtil.getObject(cacheKey, UserSession.class);
            if (cached != null) {
                return cached;
            }

            // 降级：构建基础会话（roles/permissions 为空，后续可通过 Feign 或本地查询补充）
            UserSession session = new UserSession();
            session.setUserCode(userCode);
            session.setTenantCode(tenantCode);
            session.setRoleCodes(new HashSet<>());
            session.setRoles(List.of());
            session.setPermissions(List.of());

            // 回写缓存（TTL = 3600 + random(0~300)）
            long ttl = 3600 + (int) (Math.random() * 300);
            redisUtil.setObject(cacheKey, session, ttl, java.util.concurrent.TimeUnit.SECONDS);
            return session;
        }

        // Redis 不可用，直接返回基础会话
        UserSession session = new UserSession();
        session.setUserCode(userCode);
        session.setTenantCode(tenantCode);
        session.setRoleCodes(new HashSet<>());
        session.setRoles(List.of());
        session.setPermissions(List.of());
        return session;
    }
}
