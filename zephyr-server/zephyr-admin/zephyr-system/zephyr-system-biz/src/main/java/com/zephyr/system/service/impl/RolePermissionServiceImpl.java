package com.zephyr.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.mapper.RolePermissionMapper;
import com.zephyr.system.pojo.entity.RolePermission;
import com.zephyr.system.service.IRolePermission;
import org.springframework.stereotype.Service;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-09-13
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermission {
}