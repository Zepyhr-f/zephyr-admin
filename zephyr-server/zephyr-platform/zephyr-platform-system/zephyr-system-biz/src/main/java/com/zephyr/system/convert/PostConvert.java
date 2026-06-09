package com.zephyr.system.convert;

import com.zephyr.system.pojo.entity.Post;
import com.zephyr.system.pojo.vo.PostVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostConvert {

    PostConvert INSTANCE = Mappers.getMapper(PostConvert.class);

    PostVO toVo(Post post);
    Post toEntity(PostVO postVO);
}
