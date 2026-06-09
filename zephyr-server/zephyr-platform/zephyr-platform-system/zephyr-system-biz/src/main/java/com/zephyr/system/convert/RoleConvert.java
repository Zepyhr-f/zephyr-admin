package com.zephyr.system.convert;

import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.vo.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Role 对象转换器
 *
 * @author zephyr
 */
@Mapper
public interface RoleConvert {

    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    /**
     * Role -> RoleVO
     */
    @Mappings({
            @Mapping(source = "orderNum", target = "roleSort"),
            @Mapping(target = "dataScope", ignore = true),
            @Mapping(target = "remark", ignore = true)
    })
    RoleVO toVo(Role role);
}
