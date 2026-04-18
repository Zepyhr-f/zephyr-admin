package com.zephyr.gateway.config.properties;


import java.util.List;

/**
 * 默认跳过验证配置
 *
 * @author Zephyr
 * @since 2025-09-18
 */
public class DefaultSkipProp {

    private static final List<String> DEFAULT_SKIP_URL = List.of(
            "/example",
            "/zephyr/login",
            "/oauth/token/**",
            "/oauth/captcha/**",
            "/oauth/clear-cache/**",
            "/oauth/user-info",
            "/oauth/render/**",
            "/oauth/callback/**",
            "/oauth/revoke/**",
            "/oauth/refresh/**",
            "/token/**",
            "/zephyr-auth/logout"
    );

    /**
     * 默认无需鉴权的API
     * @return
     */
    public static List<String> getDefaultSkipUrl() {
        return DEFAULT_SKIP_URL;
    }
}