package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户-角色关联表
 *
 * @author zephyr
 */
@Data
@TableName("sys_user_role")
public class UserRole implements Serializable {

    private Long userId;

    private Long roleId;
}
