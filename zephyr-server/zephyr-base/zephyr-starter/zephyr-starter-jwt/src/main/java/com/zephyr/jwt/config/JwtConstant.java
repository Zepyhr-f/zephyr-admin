package com.zephyr.jwt.config;

/**
 * jwt常量
 *
 * @author Zephyr
 * @since 2025-09-13
 */
public interface JwtConstant {
    // 支持的加密算法
    String ALGORITHM_HS256 = "HS256";
    String ALGORITHM_ES256 = "ES256";

    // token相关常量
    String TOKEN_PREFIX = "Bearer ";
    int TOKEN_PREFIX_LENGTH = 7;
    String HEADER_STRING = "Authorization";

    // token存的用户信息（仅保留用户编码、角色列表和租户编码，保持token最小化）
    String USER_CODE = "user_code";
    String ROLE_CODES = "role_codes";
    String TENANT_CODE = "tenant_code";

}