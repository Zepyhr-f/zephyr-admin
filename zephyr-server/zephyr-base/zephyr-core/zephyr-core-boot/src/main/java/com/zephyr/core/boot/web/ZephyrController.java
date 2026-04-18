package com.zephyr.core.boot.web;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static com.zephyr.core.tool.constant.WebConstants.*;

/**
 * 基础 controller
 *
 * @author Zephyr
 * @since 2025-09-13
 */
public class ZephyrController {
    private static final Long ANONYMOUS_USER_ID = -1L;
    private static final String ANONYMOUS_USER_NAME = "anonymous";
    private static final Long ANONYMOUS_ROLE_ID = -1L;


    // 当前请求对象
    protected HttpServletRequest request;

    // 请求唯一 ID（可用于日志追踪）
    protected String requestId;

    // 当前用户信息
    protected UserSession userSession;

    // 每次请求前初始化
    @ModelAttribute
    public void init() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            this.request = attributes.getRequest();
            this.requestId = request.getHeader(REQUEST_ID_HEADER);
            if (this.requestId == null || this.requestId.isEmpty()) {
                this.requestId = UUID.randomUUID().toString();
            }

            // 从请求头获取用户信息（网关已透传）
            Long userId = request.getHeader(USER_ID_HEADER) == null ?
                    ANONYMOUS_USER_ID : Long.parseLong(request.getHeader(USER_ID_HEADER));
            this.userSession.setUserId(userId);
            String username = request.getHeader(USER_NAME_HEADER) == null ?
                    ANONYMOUS_USER_NAME : request.getHeader(USER_NAME_HEADER);
            this.userSession.setUsername(request.getHeader(username));
            Long roleId = request.getHeader(ROLE_ID_HEADER) == null ?
                    ANONYMOUS_ROLE_ID : Long.parseLong(request.getHeader(ROLE_ID_HEADER));
            this.userSession.setRoleId(roleId);
        }
    }


    // 获取当前用户
    protected Long getUserId() {
        return this.userSession.getUserId();
    }

    protected String getUsername() {
        return this.userSession.getUsername();
    }

    protected Long getRoleId() {
        return this.userSession.getRoleId();
    }

    // 获取请求 ID
    protected String getRequestId() {
        return this.requestId;
    }

    // 获取原始请求对象
    protected HttpServletRequest getRequest() {
        return this.request;
    }
}