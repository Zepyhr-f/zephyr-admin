package com.zephyr.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.Post;
import com.zephyr.system.pojo.vo.PostVO;

public interface IPostService extends IService<Post> {
    IPage<PostVO> pagePost(Integer current, Integer size, String postName, Integer status);
}
