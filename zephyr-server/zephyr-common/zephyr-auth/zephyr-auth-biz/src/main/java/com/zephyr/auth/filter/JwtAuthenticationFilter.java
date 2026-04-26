package com.zephyr.auth.filter;

import com.zephyr.auth.service.ZephyrUser;
import com.zephyr.auth.service.ZephyrUserDetailsService;
import com.zephyr.core.boot.web.UserContextHolder;
import com.zephyr.core.boot.web.UserSession;
import com.zephyr.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.zephyr.jwt.config.JwtConstant.TENANT_CODE;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ZephyrUserDetailsService userDetailsService;

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();
        String userCode;

        try {
            userCode = jwtUtil.extractUserCode(token);
        } catch (Exception e) {
            log.warn("无效或过期的Token: {}", token);
            chain.doFilter(request, response);
            return;
        }

        if (userCode != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 从token中提取租户编码并设置到上下文
            String tenantCode = jwtUtil.extractClaim(token, claims -> claims.get(TENANT_CODE, String.class));
            if (tenantCode != null) {
                UserSession session = new UserSession();
                session.setTenantCode(tenantCode);
                UserContextHolder.set(session);
            }

            UserDetails userDetails = userDetailsService.loadUserByUserCode(userCode);

            if (jwtUtil.validateToken(token, ((ZephyrUser) userDetails).getUserCode())) {
                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.warn("Token验证失败: {}", token);
            }
        }

        chain.doFilter(request, response);
    }


}