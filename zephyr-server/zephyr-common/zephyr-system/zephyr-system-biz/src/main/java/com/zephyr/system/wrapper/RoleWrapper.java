package com.zephyr.system.wrapper;

import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.vo.RoleVO;

public class RoleWrapper extends BaseEntityWrapper<Role, RoleVO> {

    public static RoleWrapper build() {
        return new RoleWrapper();
    }

    @Override
    public RoleVO entityVO(Role role) {
        RoleVO roleVO = new RoleVO();
        if (role != null) {
            ZBeanUtils.copyProperties(role, roleVO);
        }
        return roleVO;
    }
}
