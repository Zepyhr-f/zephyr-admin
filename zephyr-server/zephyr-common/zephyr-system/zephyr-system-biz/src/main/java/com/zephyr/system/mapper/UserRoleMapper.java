package com.zephyr.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.system.pojo.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联 Mapper 接口
 *
 * @author zephyr
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
