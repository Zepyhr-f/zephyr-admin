package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色-菜单关联表
 *
 * @author zephyr
 */
@Data
@TableName("zephyr_sys_role_menu")
public class RoleMenu implements Serializable {

    private String roleCode;

    private String menuCode;
}
