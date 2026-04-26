package com.zephyr.auth.service;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户信息（支持多角色授权）
 *
 * @author Zephyr
 * @since 2025-09-07
 */
public class ZephyrUser implements UserDetails, CredentialsContainer {
    private final String userCode;
    private final List<String> roleCodes;   // 角色 Code 列表（如 ROLE_ADMIN）
    private final List<String> perms;       // 权限标识列表（如 sys:user:list）
    private final String username;
    private final String realName;
    private final String email;
    private final String avatar;
    private String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    private ZephyrUser(Builder builder) {
        this.userCode = builder.userCode;
        this.username = builder.username;
        this.realName = builder.realName;
        this.email = builder.email;
        this.avatar = builder.avatar;
        this.password = builder.password;
        this.roleCodes = builder.roleCodes != null ? builder.roleCodes : new ArrayList<>();
        this.perms = builder.perms != null ? builder.perms : new ArrayList<>();
        this.authorities = builder.authorities != null ? builder.authorities : new ArrayList<>();
        this.accountNonExpired = builder.accountNonExpired;
        this.accountNonLocked = builder.accountNonLocked;
        this.credentialsNonExpired = builder.credentialsNonExpired;
        this.enabled = builder.enabled;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userCode;
        private String username;
        private String realName;
        private String email;
        private String avatar;
        private String password;
        private List<Long> roleIds;
        private List<String> roleCodes;
        private List<String> perms;
        private Collection<? extends GrantedAuthority> authorities;
        private boolean accountNonExpired = true;
        private boolean accountNonLocked = true;
        private boolean credentialsNonExpired = true;
        private boolean enabled = true;

        public Builder userCode(String userCode) { this.userCode = userCode; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder realName(String realName) { this.realName = realName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder avatar(String avatar) { this.avatar = avatar; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder roleIds(List<Long> roleIds) { this.roleIds = roleIds; return this; }
        public Builder roleCodes(List<String> roleCodes) { this.roleCodes = roleCodes; return this; }
        public Builder perms(List<String> perms) { this.perms = perms; return this; }
        public Builder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }
        public Builder accountNonExpired(boolean val) { this.accountNonExpired = val; return this; }
        public Builder accountNonLocked(boolean val) { this.accountNonLocked = val; return this; }
        public Builder credentialsNonExpired(boolean val) { this.credentialsNonExpired = val; return this; }
        public Builder enabled(boolean val) { this.enabled = val; return this; }

        public ZephyrUser build() { return new ZephyrUser(this); }
    }

    // ======== UserDetails 接口实现 ========
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return accountNonExpired; }

    @Override
    public boolean isAccountNonLocked() { return accountNonLocked; }

    @Override
    public boolean isCredentialsNonExpired() { return credentialsNonExpired; }

    @Override
    public boolean isEnabled() { return enabled; }

    // ======== CredentialsContainer 接口实现 ========
    @Override
    public void eraseCredentials() { this.password = null; }

    public String getUserCode() { return userCode; }

    public List<String> getRoleCodes() { return roleCodes; }

    public List<String> getPerms() { return perms; }

    public String getRealName() { return realName; }

    public String getEmail() { return email; }

    public String getAvatar() { return avatar; }
}