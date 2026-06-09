package com.zephyr.system.mapper;

import com.zephyr.mp.mapper.ZTreeMapper;
import com.zephyr.system.pojo.entity.Menu;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
* Mapper接口
*
* @author zephyr
* @since 2025-09-24
*/
public interface MenuMapper extends ZTreeMapper<Menu> {

    @Select("SELECT DISTINCT m.* FROM zephyr_sys_menu m " +
            "INNER JOIN zephyr_sys_role_menu rm ON m.code = rm.menu_code " +
            "INNER JOIN zephyr_sys_user_role ur ON rm.role_code = ur.role_code " +
            "WHERE ur.user_code = #{userCode} AND m.tenant_code = #{tenantCode} " +
            "AND m.status = 1 AND m.if_deleted = 0 ORDER BY m.order_num ASC")
    List<Menu> selectMenusByUserCode(@org.apache.ibatis.annotations.Param("userCode") String userCode, @org.apache.ibatis.annotations.Param("tenantCode") String tenantCode);
}
