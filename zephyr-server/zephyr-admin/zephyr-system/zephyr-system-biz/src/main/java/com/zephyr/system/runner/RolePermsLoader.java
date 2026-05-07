package com.zephyr.system.runner;

import com.zephyr.redis.util.RedisUtil;
import com.zephyr.system.mapper.UserMapper;
import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.service.IRoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zephyr.redis.Constant.RedisConstant.ROLE_PREFIX;

/**
 * 角色权限加载器（已适配新版 RBAC 表结构：sys_role_menu + sys_menu.perms）
 *
 * @author Zephyr
 * @since 2025-09-13
 */
@Slf4j
@Component
@AllArgsConstructor
public class RolePermsLoader implements ApplicationRunner {
    private final IRoleService roleService;
    private final UserMapper userMapper;
    private final RedisUtil redisUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadAllRolePerms();
    }

    private void loadAllRolePerms() {
        List<Role> roleList = roleService.list();

        for (Role role : roleList) {
            // 通过 sys_role_menu + sys_menu.perms 查出该角色所有权限标识
            List<String> permsList = userMapper.selectPermsByRoleCode(role.getRoleCode(), role.getTenantCode());
            Set<String> perms = permsList.stream()
                    .filter(p -> p != null && !p.isEmpty())
                    .collect(Collectors.toSet());

            String redisKey = ROLE_PREFIX + role.getRoleCode();
            redisUtil.deleteKey(redisKey);
            if (!perms.isEmpty()) {
                redisUtil.addSet(redisKey, perms);
            }
        }
        log.info("[RolePermsLoader] 角色权限缓存加载完毕，共加载 {} 个角色", roleList.size());
    }
}