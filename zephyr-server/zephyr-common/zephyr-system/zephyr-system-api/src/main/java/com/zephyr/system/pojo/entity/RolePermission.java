package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色-权限关联（已迁移至 sys_role_menu，保留此类指向新表避免启动异常）
 *
 * @author Zephyr
 * @deprecated 请使用 {@link RoleMenu} 代替
 */
@Data
@TableName("sys_role_menu")
public class RolePermission {
    private Long roleId;
    private Long menuId;
}