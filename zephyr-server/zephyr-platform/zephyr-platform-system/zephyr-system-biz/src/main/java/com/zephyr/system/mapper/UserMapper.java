package com.zephyr.system.mapper;

import com.zephyr.mp.mapper.ZCodeMapper;
import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户 Mapper 接口
 *
 * @author zephyr
 * @since 2025-09-22
 */
public interface UserMapper extends ZCodeMapper<User> {

    /**
     * 通过 sys_user_role 关联表查询用户拥有的所有角色
     */
    @Select("SELECT r.* FROM zephyr_sys_role r " +
            "INNER JOIN zephyr_sys_user_role ur ON r.code = ur.role_code " +
            "WHERE ur.user_code = #{userCode} AND r.if_deleted = 0 AND r.status = 1 AND r.tenant_code = #{tenantCode}")
    List<Role> selectRolesByUserCode(@Param("userCode") String userCode, @Param("tenantCode") String tenantCode);

    /**
     * 通过 sys_role_menu 查询用户所有的权限标识（perms）
     */
    @Select("SELECT DISTINCT m.perms FROM zephyr_sys_menu m " +
            "INNER JOIN zephyr_sys_role_menu rm ON m.code = rm.menu_code " +
            "INNER JOIN zephyr_sys_user_role ur ON rm.role_code = ur.role_code " +
            "WHERE ur.user_code = #{userCode} AND m.tenant_code = #{tenantCode} " +
            "AND m.status = 1 AND m.if_deleted = 0 AND m.perms IS NOT NULL AND m.perms != ''")
    List<String> selectPermsByUserCode(@Param("userCode") String userCode, @Param("tenantCode") String tenantCode);

    /**
     * 通过 sys_role_menu 查询指定角色的所有权限标识（供 RolePermsLoader 缓存到 Redis）
     */
    @Select("SELECT DISTINCT m.perms FROM zephyr_sys_menu m " +
            "INNER JOIN zephyr_sys_role_menu rm ON m.code = rm.menu_code " +
            "WHERE rm.role_code = #{roleCode} AND m.tenant_code = #{tenantCode} " +
            "AND m.status = 1 AND m.if_deleted = 0 AND m.perms IS NOT NULL AND m.perms != ''")
    List<String> selectPermsByRoleCode(@Param("roleCode") String roleCode, @Param("tenantCode") String tenantCode);
}
