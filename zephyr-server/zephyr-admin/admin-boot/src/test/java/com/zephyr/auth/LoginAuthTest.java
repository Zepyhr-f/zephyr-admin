package com.zephyr.auth;

import com.zephyr.common.AdminApplication;
import com.zephyr.jwt.util.JwtUtil;
import com.zephyr.redis.util.RedisUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assumptions;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 登录认证核心功能单元测试
 *
 * 覆盖：
 * 1. Access Token / Refresh Token 生成与解析
 * 2. JWT Payload 规范（sub/tid/jti/iat/exp）
 * 3. Token 过期与剩余时间计算
 * 4. Token 黑名单（需 Redis）
 * 5. 网关签名生成与校验
 *
 * @author Zephyr
 * @since 2025-04-28
 */
@SpringBootTest(classes = AdminApplication.class)
public class LoginAuthTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private RedisUtil redisUtil;

    private static final String TEST_USER_CODE = "U001";
    private static final String TEST_TENANT_CODE = "T001";
    private static final String GATEWAY_SECRET = "zephyr-gateway-secret-key-2026";

    @BeforeEach
    void setUp() {
        // 若 Redis 不可用，仅跳过依赖 Redis 的用例，不影响其他测试
    }

    // ==================== JWT 生成与解析 ====================

    @Test
    void testAccessTokenGeneration() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_code", TEST_USER_CODE);
        claims.put("tid", TEST_TENANT_CODE);

        String token = jwtUtil.generateToken(claims, 2 * 60 * 60 * 1000L); // 2小时

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // header.payload.signature

        // 解析校验
        String userCode = jwtUtil.extractUserCode(token);
        String tenantCode = jwtUtil.extractTenantCode(token);
        String jti = jwtUtil.extractJti(token);

        assertEquals(TEST_USER_CODE, userCode);
        assertEquals(TEST_TENANT_CODE, tenantCode);
        assertNotNull(jti);
        assertFalse(jti.isEmpty());
    }

    @Test
    void testRefreshTokenGeneration() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_code", TEST_USER_CODE);
        claims.put("tid", TEST_TENANT_CODE);

        String refreshToken = jwtUtil.generateToken(claims, 7 * 24 * 60 * 60 * 1000L); // 7天

        assertNotNull(refreshToken);
        assertNotEquals("", refreshToken);

        String jti = jwtUtil.extractJti(refreshToken);
        assertNotNull(jti);

        // Refresh Token 同样使用 sub 存储 userCode
        assertEquals(TEST_USER_CODE, jwtUtil.extractUserCode(refreshToken));
    }

    @Test
    void testTokenPayloadClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_code", TEST_USER_CODE);
        claims.put("tid", TEST_TENANT_CODE);

        String token = jwtUtil.generateToken(claims, 3600000);
        Claims allClaims = jwtUtil.extractAllClaims(token);

        assertEquals(TEST_USER_CODE, allClaims.getSubject());           // sub
        assertEquals(TEST_TENANT_CODE, allClaims.get("tid"));           // tid
        assertNotNull(allClaims.get("jti"));                             // jti
        assertNotNull(allClaims.getIssuedAt());                          // iat
        assertNotNull(allClaims.getExpiration());                        // exp
    }

    @Test
    void testTokenExpiration() {
        Map<String, Object> claims = Map.of("user_code", TEST_USER_CODE);
        // 生成一个已过期或即将过期的 token（1ms）
        String expiredToken = jwtUtil.generateToken(claims, 1L);

        // 稍微等待确保过期
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(jwtUtil.isTokenExpired(expiredToken));
    }

    @Test
    void testTokenRemainingTime() {
        Map<String, Object> claims = Map.of("user_code", TEST_USER_CODE);
        String token = jwtUtil.generateToken(claims, 3600000); // 1小时

        long remaining = jwtUtil.getTokenRemainingTime(token);
        assertTrue(remaining > 0);
        assertTrue(remaining <= 3600000);
    }

    @Test
    void testTokenValidation() {
        Map<String, Object> claims = Map.of("user_code", TEST_USER_CODE);
        String token = jwtUtil.generateToken(claims, 3600000);

        assertTrue(jwtUtil.validateToken(token, TEST_USER_CODE));
        assertFalse(jwtUtil.validateToken(token, "U999")); // 错误的 userCode
    }

    // ==================== Token 黑名单（依赖 Redis） ====================

    @Test
    void testTokenBlacklist() {
        Assumptions.assumeTrue(redisUtil != null, "Redis 未配置，跳过黑名单测试");

        Map<String, Object> claims = Map.of("user_code", TEST_USER_CODE);
        String token = jwtUtil.generateToken(claims, 3600000);
        String jti = jwtUtil.extractJti(token);

        // 模拟黑名单写入（登出逻辑）
        long remainingTime = jwtUtil.getTokenRemainingTime(token);
        redisUtil.setString("blacklist:" + jti, "1", remainingTime, TimeUnit.MILLISECONDS);

        // 校验黑名单存在
        String blacklisted = redisUtil.getString("blacklist:" + jti);
        assertEquals("1", blacklisted);

        // 清理
        redisUtil.deleteKey("blacklist:" + jti);
    }

    @Test
    void testRefreshTokenStorage() {
        Assumptions.assumeTrue(redisUtil != null, "Redis 未配置，跳过刷新 Token 存储测试");

        Map<String, Object> claims = Map.of("user_code", TEST_USER_CODE, "tid", TEST_TENANT_CODE);
        String refreshToken = jwtUtil.generateToken(claims, 7 * 24 * 60 * 60 * 1000L);
        String jti = jwtUtil.extractJti(refreshToken);

        String redisValue = TEST_USER_CODE + ":" + TEST_TENANT_CODE;
        redisUtil.setString("refresh:" + jti, redisValue, 7, TimeUnit.DAYS);

        String stored = redisUtil.getString("refresh:" + jti);
        assertEquals(redisValue, stored);

        // 清理
        redisUtil.deleteKey("refresh:" + jti);
    }

    // ==================== 网关签名 ====================

    @Test
    void testGatewaySignGeneration() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = generateGatewaySign(TEST_USER_CODE, TEST_TENANT_CODE, timestamp, GATEWAY_SECRET);

        assertNotNull(sign);
        assertFalse(sign.isEmpty());

        // 相同输入应产生相同签名
        String sign2 = generateGatewaySign(TEST_USER_CODE, TEST_TENANT_CODE, timestamp, GATEWAY_SECRET);
        assertEquals(sign, sign2);
    }

    @Test
    void testGatewaySignVerification() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = generateGatewaySign(TEST_USER_CODE, TEST_TENANT_CODE, timestamp, GATEWAY_SECRET);

        // 正确签名校验通过
        assertTrue(verifyGatewaySign(TEST_USER_CODE, TEST_TENANT_CODE, timestamp, sign, GATEWAY_SECRET));

        // 错误密钥校验失败
        assertFalse(verifyGatewaySign(TEST_USER_CODE, TEST_TENANT_CODE, timestamp, sign, "wrong-secret"));

        // 篡改时间戳校验失败
        assertFalse(verifyGatewaySign(TEST_USER_CODE, TEST_TENANT_CODE, "1234567890", sign, GATEWAY_SECRET));
    }

    @Test
    void testTimestampAntiReplay() {
        // 模拟过期时间戳（超过 5 分钟）
        long oldTimestamp = System.currentTimeMillis() / 1000 - 400;
        String sign = generateGatewaySign(TEST_USER_CODE, TEST_TENANT_CODE, String.valueOf(oldTimestamp), GATEWAY_SECRET);

        assertTrue(verifyGatewaySign(TEST_USER_CODE, TEST_TENANT_CODE, String.valueOf(oldTimestamp), sign, GATEWAY_SECRET));
        assertFalse(isTimestampValid(oldTimestamp)); // 超过 300 秒窗口
    }

    // ==================== 辅助方法 ====================

    private String generateGatewaySign(String userCode, String tenantCode, String timestamp, String secret) {
        try {
            String data = String.valueOf(userCode) + String.valueOf(tenantCode) + timestamp;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("签名生成失败", e);
        }
    }

    private boolean verifyGatewaySign(String userCode, String tenantCode, String timestamp, String sign, String secret) {
        try {
            String expected = generateGatewaySign(userCode, tenantCode, timestamp, secret);
            return expected.equals(sign);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTimestampValid(long timestamp) {
        long now = System.currentTimeMillis() / 1000;
        return Math.abs(now - timestamp) <= 300;
    }
}
