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
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @PostMapping("/login")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "登录", description = "传入用户信息")
    public R<LoginResponse> login(@RequestBody LoginRequest request) {
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
            Map<String, Object> claims = new HashMap<>();
            claims.put(USER_CODE, userDetails.getUserCode());
            // 存储 role_codes（逗号分隔的字符串格式）供网关透传和后端解析
            if (userDetails.getRoleCodes() != null && !userDetails.getRoleCodes().isEmpty()) {
                String roleCodesStr = String.join(",", userDetails.getRoleCodes());
                claims.put(ROLE_CODES, roleCodesStr);
            }
            // 存储租户编码
            if (StringUtils.isNotBlank(request.getTenantCode())) {
                claims.put(TENANT_CODE, request.getTenantCode());
            }

            String token = jwtUtil.generateToken(claims);
            redisUtil.setString(RedisConstant.TOKEN_PREFIX + userDetails.getUserCode(), token, 1, TimeUnit.HOURS);

            return R.data(LoginResponse.builder()
                    .token(token)
                    .refreshToken(token) // 暂时使用同一个token
                    .build());
        } catch (BadCredentialsException e) {
            return R.fail("用户名或密码错误");
        } catch (AuthenticationException e) {
            return R.fail("认证失败: " + e.getMessage());
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

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("user", user);
            userInfo.put("roles", roleCodes);
            userInfo.put("buttons", perms);

            return R.data(userInfo);
        } catch (Exception e) {
            return R.fail("Token解析失败: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperationSupport(order = 2)
    @Operation(summary = "登出", description = "传入登录token")
    public R<String> logout(HttpServletRequest request) {
        try{
            String token = request.getHeader(HEADER_STRING);
            token = token.substring(TOKEN_PREFIX_LENGTH);
            String userCode = jwtUtil.extractUserCode(token);

            redisUtil.deleteKey(RedisConstant.TOKEN_PREFIX + userCode);
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
