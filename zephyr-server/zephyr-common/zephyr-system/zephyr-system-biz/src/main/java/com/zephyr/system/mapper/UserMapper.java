package com.zephyr.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.system.pojo.entity.User;
import com.zephyr.system.pojo.entity.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户 Mapper 接口
 *
 * @author zephyr
 * @since 2025-09-22
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过 sys_user_role 关联表查询用户拥有的所有角色
     */
    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.role_code = ur.role_code " +
            "WHERE ur.user_code = #{userCode} AND r.del_flag = 0 AND r.status = 1 AND r.tenantCode = #{tenantCode}")
    List<Role> selectRolesByUserCode(String userCode, String tenantCode);

    /**
     * 通过 sys_role_menu 查询用户所有的权限标识（perms）
     */
    @Select("SELECT DISTINCT m.perms FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.perms IS NOT NULL AND m.perms != ''")
    List<String> selectPermsByUserCode(String userCode, String tenantCode);

    /**
     * 通过 sys_role_menu 查询指定角色的所有权限标识（供 RolePermsLoader 缓存到 Redis）
     */
    @Select("SELECT DISTINCT m.perms FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId} AND m.perms IS NOT NULL AND m.perms != ''")
    List<String> selectPermsByRoleCode(String userCode, String tenantCode);
}
