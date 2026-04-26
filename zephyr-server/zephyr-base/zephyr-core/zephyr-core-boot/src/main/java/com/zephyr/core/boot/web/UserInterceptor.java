package com.zephyr.core.boot.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashSet;
import java.util.List;

import static com.zephyr.core.tool.constant.WebConstants.*;

public class UserInterceptor implements HandlerInterceptor {
    private static final String ANONYMOUS_USER_CODE = "-1";
    private static final String ANONYMOUS_ROLE_CODE = "-1";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserSession session = new UserSession();

        String userCode = request.getHeader(USER_CODE_HEADER);
        session.setUserCode(userCode == null ? ANONYMOUS_USER_CODE : userCode);

        String roleCodesStr = request.getHeader(ROLE_CODE_HEADER);
        if (roleCodesStr == null || roleCodesStr.isEmpty()) {
            session.setRoleCodes(new HashSet<>(List.of(ANONYMOUS_ROLE_CODE)));
        } else {
            session.setRoleCodes(new HashSet<>(List.of(roleCodesStr.split(","))));
        }

        session.setTenantCode(request.getHeader(TENANT_CODE_HEADER));

        UserContextHolder.set(session);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        // 请求结束必须清理，防止内存泄漏和线程池复用污染
        UserContextHolder.clear();
    }
}
