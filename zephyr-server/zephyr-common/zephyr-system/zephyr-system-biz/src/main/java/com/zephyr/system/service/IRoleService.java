package com.zephyr.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.Role;

import java.util.List;

/**
 * 角色 Service 接口
 *
 * @author zephyr
 */
public interface IRoleService extends IService<Role> {

    /**
     * 查询全部启用角色（用于下拉/弹窗分配）
     */
    List<Role> listAllEnabled();

    /**
     * 权限授权：保存角色与菜单的关系
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     */
    boolean assignMenus(Long roleId, List<Long> menuIds);

    /**
     * 查询角色已分配的菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 更新角色状态
     */
    boolean updateStatus(Long roleId, Integer status);
}
