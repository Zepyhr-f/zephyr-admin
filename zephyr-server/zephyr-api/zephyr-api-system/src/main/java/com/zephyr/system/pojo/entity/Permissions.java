package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限（已迁移至大一统 sys_menu 表，保留此类指向 sys_menu 避免启动异常）
 *
 * @author Zephyr
 * @deprecated 请使用 {@link Menu} 代替
 */
@Data
@TableName("sys_menu")
public class Permissions {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String perms;
    private String menuName;
    private String menuType;
    private LocalDateTime createTime;
}