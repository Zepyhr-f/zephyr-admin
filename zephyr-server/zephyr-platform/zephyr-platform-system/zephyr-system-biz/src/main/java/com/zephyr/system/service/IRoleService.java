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
     * @param code 角色编码
     * @param menuCodeList 菜单编码列表
     */
    boolean assignMenus(String code, List<String> menuCodeList);

    /**
     * 查询角色已分配的菜单编码列表
     */
    List<String> getMenuCodesByRoleCode(String code);

    /**
     * 更新角色状态
     */
    boolean updateStatus(Long roleId, Integer status);
}
