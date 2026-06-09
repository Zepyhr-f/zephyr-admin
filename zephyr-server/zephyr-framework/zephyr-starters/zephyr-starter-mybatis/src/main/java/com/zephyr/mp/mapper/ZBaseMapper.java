package com.zephyr.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.mp.base.BaseEntity;

/**
 * 基础 Mapper 接口
 * <p>
 * 所有 Mapper 的顶级父接口，继承 MyBatis-Plus 的 {@link BaseMapper}。
 * 绑定实体类型必须继承 {@link BaseEntity}，确保所有业务实体具备统一的通用字段（id、tenantCode、createTime 等）。
 *
 * @param <T> 实体类型
 * @author Zephyr
 * @since 2025-09-23
 */
public interface ZBaseMapper<T extends BaseEntity> extends BaseMapper<T> {

}
