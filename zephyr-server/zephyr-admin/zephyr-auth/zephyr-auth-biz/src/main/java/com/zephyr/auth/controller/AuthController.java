package com.zephyr.auth.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zephyr.auth.pojo.dto.LoginRequest;
import com.zephyr.auth.pojo.vo.LoginResponse;
import com.zephyr.auth.service.ZephyrUser;
import com.zephyr.core.boot.web.UserContextHolder;
import com.zephyr.core.boot.web.UserSession;
import com.zephyr.core.tool.api.R;
import com.zephyr.jwt.util.JwtUtil;
import com.zephyr.redis.Constant.RedisConstant;
import com.zephyr.redis.util.RedisUtil;
import com.zephyr.system.pojo.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.zephyr.system.feign.IUserClient;

import static com.zephyr.jwt.config.JwtConstant.*;

/**
 * 登录登出
 *
 * @author Zephyr
 * @since 2025-09-13
 */
@RestController
@AllArgsConstructor
@Tag(name = "登录/登出", description = "登录/登出")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final IUserClient userClient;

    // Access Token 默认 2 小时，Refresh Token 7 天
    private static final long ACCESS_TOKEN_EXPIRATION = 2 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    @PostMapping("/login")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "登录", description = "传入用户信息")
    public R<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            // 设置租户上下文，供 UserDetailsService 使用
            if (StringUtils.isNotBlank(request.getTenantCode())) {
                UserSession session = new UserSession();
                session.setTenantCode(request.getTenantCode());
                UserContextHolder.set(session);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            ZephyrUser userDetails = (ZephyrUser) authentication.getPrincipal();
            String tenantCode = StringUtils.defaultString(request.getTenantCode());

            Map<String, Object> claims = new HashMap<>();
            claims.put(USER_CODE, userDetails.getUserCode());
            claims.put(TENANT_CODE, tenantCode);

            // 生成 Access Token（2小时）
            String accessToken = jwtUtil.generateToken(claims, ACCESS_TOKEN_EXPIRATION);

            // 生成 Refresh Token（7天）
            String refreshToken = jwtUtil.generateToken(claims, REFRESH_TOKEN_EXPIRATION);
            String refreshJti = jwtUtil.extractJti(refreshToken);

            // Refresh Token 存入 Redis
            String redisValue = userDetails.getUserCode() + ":" + tenantCode;
            redisUtil.setString(RedisConstant.REFRESH_PREFIX + refreshJti, redisValue, REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);

            // Refresh Token 写入 HttpOnly Cookie
            ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(Duration.ofMillis(REFRESH_TOKEN_EXPIRATION))
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());

            return R.data(LoginResponse.builder()
                    .token(accessToken)
                    .build());
        } catch (BadCredentialsException e) {
            return R.fail("用户名或密码错误");
        } catch (AuthenticationException e) {
            return R.fail("认证失败: " + e.getMessage());
        }
    }

    @PostMapping("/auth/refresh")
    @Operation(summary = "刷新Access Token", description = "通过HttpOnly Cookie中的Refresh Token换取新的Access Token")
    public R<LoginResponse> refreshToken(@CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
                                          HttpServletResponse response) {
        if (StringUtils.isBlank(refreshToken)) {
            return R.fail("Refresh Token 缺失");
        }

        try {
            String jti = jwtUtil.extractJti(refreshToken);
            if (StringUtils.isBlank(jti)) {
                return R.fail("无效的 Refresh Token");
            }

            // 校验 Redis 中是否存在
            String redisValue = redisUtil.getString(RedisConstant.REFRESH_PREFIX + jti);
            if (StringUtils.isBlank(redisValue)) {
                return R.fail("Refresh Token 已失效");
            }

            // 解析 userCode 和 tenantCode
            String[] parts = redisValue.split(":");
            String userCode = parts[0];
            String tenantCode = parts.length > 1 ? parts[1] : "";

            // 生成新的 Access Token
            Map<String, Object> claims = new HashMap<>();
            claims.put(USER_CODE, userCode);
            claims.put(TENANT_CODE, tenantCode);
            String newAccessToken = jwtUtil.generateToken(claims, ACCESS_TOKEN_EXPIRATION);

            // 单点登录模式：覆盖旧 refresh token（生成新的）
            String newRefreshToken = jwtUtil.generateToken(claims, REFRESH_TOKEN_EXPIRATION);
            String newRefreshJti = jwtUtil.extractJti(newRefreshToken);

            // 删除旧的，写入新的
            redisUtil.deleteKey(RedisConstant.REFRESH_PREFIX + jti);
            redisUtil.setString(RedisConstant.REFRESH_PREFIX + newRefreshJti, redisValue, REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);

            // 更新 Cookie
            ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, newRefreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(Duration.ofMillis(REFRESH_TOKEN_EXPIRATION))
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());

            return R.data(LoginResponse.builder().token(newAccessToken).build());
        } catch (Exception e) {
            return R.fail("Token 刷新失败: " + e.getMessage());
        }
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "从 token 中提取并在数据库查询")
    public R<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        String authHeader = request.getHeader(HEADER_STRING);
        if (StringUtils.isBlank(authHeader)) {
            return R.fail("未检测到登录令牌 (Header missing)");
        }

        String token = authHeader.startsWith(TOKEN_PREFIX) ?
                authHeader.substring(TOKEN_PREFIX_LENGTH) : authHeader;

        try {
            String userCode = jwtUtil.extractUserCode(token);
            String tenantCode = getTenantCode();
            User user = userClient.getUserByUserCode(userCode, tenantCode);

            if (user == null) {
                return R.fail("用户不存在");
            }

            // 从关联表查询真实的多角色列表
            List<String> roleCodes = userClient.getRolesByUserCode(userCode, tenantCode);

            // 从关联表查询真实的权限标识列表
            List<String> perms = userClient.getPermsByUserCode(userCode, tenantCode);

            Map<String, Object> userInfo = new LinkedHashMap<>();
            Map<String, Object> userMap = new LinkedHashMap<>();
            userMap.put("userCode", user.getUserCode());
            userMap.put("username", user.getUserName());
            userInfo.put("user", userMap);
            userInfo.put("roles", roleCodes);
            userInfo.put("permissions", perms);

            return R.data(userInfo);
        } catch (Exception e) {
            return R.fail("Token解析失败: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperationSupport(order = 2)
    @Operation(summary = "登出", description = "传入登录token")
    public R<String> logout(HttpServletRequest request,
                            @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken,
                            HttpServletResponse response) {
        try {
            // 1. 拉黑 Access Token
            String authHeader = request.getHeader(HEADER_STRING);
            if (StringUtils.isNotBlank(authHeader) && authHeader.length() > TOKEN_PREFIX_LENGTH) {
                String token = authHeader.substring(TOKEN_PREFIX_LENGTH);
                String jti = jwtUtil.extractJti(token);
                if (StringUtils.isNotBlank(jti)) {
                    long remainingTime = jwtUtil.getTokenRemainingTime(token);
                    if (remainingTime > 0) {
                        redisUtil.setString(RedisConstant.BLACKLIST_PREFIX + jti, "1", remainingTime, TimeUnit.MILLISECONDS);
                    }
                }
            }

            // 2. 销毁 Refresh Token
            if (StringUtils.isNotBlank(refreshToken)) {
                String refreshJti = jwtUtil.extractJti(refreshToken);
                if (StringUtils.isNotBlank(refreshJti)) {
                    redisUtil.deleteKey(RedisConstant.REFRESH_PREFIX + refreshJti);
                }
            }

            // 3. 清空 Cookie
            ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/")
                    .maxAge(Duration.ZERO)
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
        } catch (Exception e) {
            return R.fail("登出失败: " + e.getMessage());
        }

        return R.success("登出成功");
    }

    private String getTenantCode() {
        UserSession session = UserContextHolder.get();
        return session != null ? session.getTenantCode() : null;
    }
}
