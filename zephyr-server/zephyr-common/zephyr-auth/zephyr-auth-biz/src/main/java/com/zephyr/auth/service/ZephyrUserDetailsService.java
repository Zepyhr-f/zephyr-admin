package com.zephyr.auth.service;

import com.zephyr.system.feign.IUserClient;
import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userClient.getUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 从关联表查询该用户的所有角色
        List<Role> roles = userClient.getRolesByUserId(user.getId());
        List<Long> roleIds = roles.stream()
                .map(Role::getId)
                .collect(Collectors.toList());
        List<String> roleCodes = roles.stream()
                .map(Role::getRoleCode)
                .collect(Collectors.toList());

        // 从关联表查询该用户的所有权限标识
        List<String> perms = userClient.getPermsByUserId(user.getId());

        // 构建 Spring Security 的 GrantedAuthority（角色+权限合并）
        List<SimpleGrantedAuthority> authorities = roleCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return ZephyrUser.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roleIds(roleIds)
                .roleCodes(roleCodes)
                .perms(perms)
                .authorities(authorities)
                .enabled(user.getStatus() == null || user.getStatus() == 1)
                .build();
    }
}