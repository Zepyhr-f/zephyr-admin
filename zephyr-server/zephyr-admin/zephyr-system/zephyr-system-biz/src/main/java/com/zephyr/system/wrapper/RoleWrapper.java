package com.zephyr.system.wrapper;

import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.system.convert.RoleConvert;
import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.vo.RoleVO;

public class RoleWrapper extends BaseEntityWrapper<Role, RoleVO> {

    private final RoleConvert roleConvert = RoleConvert.INSTANCE;

    public static RoleWrapper build() {
        return new RoleWrapper();
    }

    @Override
    public RoleVO entityVO(Role role) {
        if (role == null) {
            return null;
        }
        return roleConvert.toVo(role);
    }
}
