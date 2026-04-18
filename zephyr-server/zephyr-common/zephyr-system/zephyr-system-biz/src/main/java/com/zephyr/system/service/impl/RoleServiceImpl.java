package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.mapper.RoleMapper;
import com.zephyr.system.mapper.RoleMenuMapper;
import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.entity.RoleMenu;
import com.zephyr.system.service.IRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色 Service 实现
 *
 * @author zephyr
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMenuMapper roleMenuMapper;

    @Override
    public List<Role> listAllEnabled() {
        return list(new LambdaQueryWrapper<Role>()
                .eq(Role::getStatus, 1)
                .orderByAsc(Role::getRoleSort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenus(Long roleId, List<Long> menuIds) {
        // 先清空旧关系
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getRoleId, roleId));
        // 批量插入新关系
        if (menuIds != null && !menuIds.isEmpty()) {
            List<RoleMenu> records = menuIds.stream()
                    .map(menuId -> new RoleMenu(roleId, menuId))
                    .collect(Collectors.toList());
            records.forEach(roleMenuMapper::insert);
        }
        return true;
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return roleMenuMapper.selectList(new LambdaQueryWrapper<RoleMenu>()
                        .eq(RoleMenu::getRoleId, roleId))
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateStatus(Long roleId, Integer status) {
        Role role = new Role();
        role.setId(roleId);
        role.setStatus(status);
        return updateById(role);
    }
}
