package com.zephyr.system.convert;

import com.zephyr.system.pojo.entity.Menu;
import com.zephyr.system.pojo.vo.MenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Menu 对象转换器
 *
 * @author zephyr
 */
@Mapper
public interface MenuConvert {

    MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

    /**
     * Menu -> MenuVO
     */
    @Mappings({
            @Mapping(target = "children", ignore = true)
    })
    MenuVO toVo(Menu menu);
}
