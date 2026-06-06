package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.system.pojo.entity.Post;
import com.zephyr.system.pojo.vo.PostVO;
import com.zephyr.system.service.IPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "岗位管理")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    @Operation(summary = "获取岗位列表")
    @GetMapping("/list")
    public R<IPage<PostVO>> list(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "postName", required = false) String postName,
            @RequestParam(value = "status", required = false) Integer status) {
        IPage<PostVO> page = postService.pagePost(current, size, postName, status);
        return R.data(page);
    }

    @Operation(summary = "新增岗位")
    @PostMapping("/save")
    public R<Boolean> save(@RequestBody Post post) {
        return R.status(postService.save(post));
    }

    @Operation(summary = "修改岗位")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody Post post) {
        return R.status(postService.updateById(post));
    }

    @Operation(summary = "删除岗位")
    @PostMapping("/remove")
    public R<Boolean> remove(@RequestBody List<String> codes) {
        return R.status(postService.remove(new LambdaQueryWrapper<Post>().in(Post::getCode, codes)));
    }

    @Operation(summary = "更新岗位状态")
    @PostMapping("/status")
    public R<Boolean> updateStatus(@RequestBody Post post) {
        return R.status(postService.updateById(post));
    }
}
