package com.zephyr.system.convert;

import com.zephyr.system.pojo.entity.Dept;
import com.zephyr.system.pojo.vo.DeptVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Dept 对象转换器
 *
 * @author zephyr
 */
@Mapper
public interface DeptConvert {

    DeptConvert INSTANCE = Mappers.getMapper(DeptConvert.class);

    /**
     * Dept -> DeptVO
     */
    @Mappings({
            @Mapping(target = "children", ignore = true)
    })
    DeptVO toVo(Dept dept);
}
