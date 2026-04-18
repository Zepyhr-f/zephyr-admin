package com.zephyr.auth.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zephyr.auth.pojo.dto.LoginRequest;
import com.zephyr.auth.pojo.vo.LoginResponse;
import com.zephyr.auth.service.ZephyrUser;
import com.zephyr.core.tool.api.R;
import com.zephyr.jwt.util.JwtUtil;
import com.zephyr.redis.Constant.RedisConstant;
import com.zephyr.redis.util.RedisUtil;
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
import java.util.stream.Collectors;
import com.zephyr.system.feign.IUserClient;
import com.zephyr.system.pojo.entity.User;

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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            ZephyrUser userDetails = (ZephyrUser) authentication.getPrincipal();
            Map<String, Object> claims = new HashMap<>();
            claims.put(USER_ID, userDetails.getUserId());
            claims.put(USER_NAME, userDetails.getUsername());
            if (userDetails.getRoleIds() != null && !userDetails.getRoleIds().isEmpty()) {
                Long primaryRoleId = userDetails.getRoleIds().get(0);
                claims.put("role_id", primaryRoleId);
                System.out.println("DEBUG: User " + userDetails.getUsername() + " assigned primary role_id: " + primaryRoleId);
            } else {
                System.out.println("DEBUG: User " + userDetails.getUsername() + " has NO role_ids assigned!");
            }

            String token = jwtUtil.generateToken(claims);
            redisUtil.setString(RedisConstant.TOKEN_PREFIX + userDetails.getUsername(), token, 1, TimeUnit.HOURS);

            return R.data(LoginResponse.builder()
                    .token(token)
                    .refreshToken(token) // 暂时使用同一个token
                    .userId(userDetails.getUserId().toString())
                    .message("登录成功")
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
            String username = jwtUtil.extractUsername(token);
            User user = userClient.getUserByUserName(username);

            if (user == null) {
                return R.fail("用户不存在");
            }

            // 从关联表查询真实的多角色列表
            List<String> roleCodes = userClient.getRolesByUserId(user.getId()).stream()
                    .map(role -> role.getRoleCode())
                    .collect(java.util.stream.Collectors.toList());

            // 从关联表查询真实的权限标识列表
            List<String> perms = userClient.getPermsByUserId(user.getId());

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUsername());
            userInfo.put("realName", user.getRealName());
            userInfo.put("email", user.getEmail());
            userInfo.put("avatar", "");
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
            String username = jwtUtil.extractUsername(token);

            redisUtil.deleteKey(RedisConstant.TOKEN_PREFIX + username);
        } catch (Exception e) {
            return R.fail("登出失败: " + e.getMessage());
        }

        return R.success("登出成功");
    }
}