package com.zephyr.auth.service;

import com.zephyr.core.boot.web.UserContextHolder;
import com.zephyr.system.feign.IUserClient;
import com.zephyr.system.pojo.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息加载（支持多角色授权）
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@Service
@AllArgsConstructor
public class ZephyrUserDetailsService implements UserDetailsService {

    private final IUserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        String tenantCode = getTenantCode();
        User user = userClient.getUserByCode(userCode, tenantCode);
        return buildZephyrUser(user, userCode, tenantCode);
    }

    public UserDetails loadUserByUserCode(String userCode) throws UsernameNotFoundException {
        return loadUserByUsername(userCode);
    }

    private String getTenantCode() {
        com.zephyr.core.boot.web.UserSession session = UserContextHolder.get();
        return session != null ? session.getTenantCode() : null;
    }

    private ZephyrUser buildZephyrUser(User user, String userCode, String tenantCode) {
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 从关联表查询该用户的所有角色
        List<String> roleCodes = userClient.getRolesByUserCode(userCode, tenantCode);

        // 从关联表查询该用户的所有权限标识
        List<String> perms = userClient.getPermsByUserCode(userCode, tenantCode);

        // 构建 Spring Security 的 GrantedAuthority（角色+权限合并）
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(roleCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        authorities.addAll(perms.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));

        return ZephyrUser.builder()
                .userCode(user.getCode())
                .tenantCode(user.getTenantCode())
                .password(user.getPassword())
                .authorities(authorities)
                .enabled(user.getStatus() == null || user.getStatus() == 1)
                .build();
    }
}
