package com.zephyr.system.convert;

import com.zephyr.system.pojo.entity.User;
import com.zephyr.system.pojo.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * User 对象转换器
 *
 * @author zephyr
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * User -> UserVO
     */
    @Mappings({
            @Mapping(target = "deptName", ignore = true),
            @Mapping(target = "roleCodes", ignore = true)
    })
    UserVO toVo(User user);
}
