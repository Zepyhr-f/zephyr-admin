package com.zephyr.auth.feign;


import com.zephyr.core.boot.web.UserSession;
import com.zephyr.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@FeignClient(value = "zephyr-common")
public interface IAuthClient {

    String API_PREFIX = "/feign/client/auth";// token获取
    String TOKEN_VI = API_PREFIX + "/token/validate";

    /**
     * 验证token
     */
    @PostMapping(TOKEN_VI)
    R<UserSession> parseToken(@RequestHeader("Authorization") String authHeader);
}