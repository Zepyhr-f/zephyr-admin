package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.convert.PostConvert;
import com.zephyr.system.mapper.PostMapper;
import com.zephyr.system.pojo.entity.Post;
import com.zephyr.system.pojo.vo.PostVO;
import com.zephyr.system.service.IPostService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {

    @Override
    public IPage<PostVO> pagePost(Integer current, Integer size, String postName, Integer status) {
        Page<Post> page = new Page<>(current, size);
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<Post>()
                .like(StringUtils.hasText(postName), Post::getPostName, postName)
                .eq(status != null, Post::getStatus, status)
                .orderByAsc(Post::getOrderNum);
                
        Page<Post> postPage = this.page(page, wrapper);
        return postPage.convert(PostConvert.INSTANCE::toVo);
    }
}
