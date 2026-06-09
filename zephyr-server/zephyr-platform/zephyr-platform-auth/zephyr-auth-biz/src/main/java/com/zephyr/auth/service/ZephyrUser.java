package com.zephyr.auth.service;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 认证用户信息
 * <p>
 * 仅保留核心认证信息（userCode、tenantCode），不承载业务属性。
 * 角色、权限、姓名、邮箱等业务数据通过服务层实时查询，避免认证对象臃肿。
 *
 * @author Zephyr
 * @since 2025-09-07
 */
public class ZephyrUser implements UserDetails, CredentialsContainer {

    private final String userCode;
    private final String tenantCode;
    private String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    private ZephyrUser(Builder builder) {
        this.userCode = builder.userCode;
        this.tenantCode = builder.tenantCode;
        this.password = builder.password;
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
        private String tenantCode;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;
        private boolean accountNonExpired = true;
        private boolean accountNonLocked = true;
        private boolean credentialsNonExpired = true;
        private boolean enabled = true;

        public Builder userCode(String userCode) { this.userCode = userCode; return this; }
        public Builder tenantCode(String tenantCode) { this.tenantCode = tenantCode; return this; }
        public Builder password(String password) { this.password = password; return this; }
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

    /**
     * Spring Security 的 username 直接返回 userCode
     */
    @Override
    public String getUsername() { return userCode; }

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

    // ======== 核心认证字段 ========
    public String getUserCode() { return userCode; }

    public String getTenantCode() { return tenantCode; }
}
