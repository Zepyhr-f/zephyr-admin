package com.zephyr.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.mapper.PermissionsMapper;
import com.zephyr.system.pojo.entity.Permissions;
import com.zephyr.system.service.IPermissions;
import org.springframework.stereotype.Service;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-09-13
 */
@Service
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements IPermissions {
}