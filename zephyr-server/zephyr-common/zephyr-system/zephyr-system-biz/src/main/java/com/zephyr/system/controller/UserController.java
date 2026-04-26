package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.system.pojo.entity.User;
import com.zephyr.system.pojo.vo.UserVO;
import com.zephyr.system.service.IUserService;
import com.zephyr.system.wrapper.UserWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 *
 * @author zephyr
 */
@AllArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final IUserService service;

    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "详情", description = "传入id")
    public R<UserVO> detail(@RequestParam("id") Long id) {
        User user = service.getById(id);
        return R.data(UserWrapper.build().entityVO(user));
    }

    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "用户列表", description = "支持按用户名、手机号、状态、部门筛选")
    public R<List<UserVO>> list(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "deptCode", required = false) String deptCode) {
        return R.data(service.listWithDept(username, phone, status, deptCode));
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增", description = "传入用户实体")
    public R<Boolean> save(@Valid @RequestBody User user) {
        return R.status(service.save(user));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "修改", description = "传入用户实体")
    public R<Boolean> update(@Valid @RequestBody User user) {
        return R.status(service.updateById(user));
    }

    @PostMapping("/submit")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "新增或修改", description = "传入用户实体")
    public R<Boolean> submit(@Valid @RequestBody User user) {
        return R.status(service.saveOrUpdate(user));
    }

    @PostMapping("/updateStatus")
    @ApiOperationSupport(order = 6)
    @Operation(summary = "更新状态", description = "传入id和status")
    public R<Boolean> updateStatus(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("id").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        return R.status(service.updateStatus(userId, status));
    }

    @PostMapping("/resetPassword")
    @ApiOperationSupport(order = 7)
    @Operation(summary = "重置密码", description = "重置为默认密码123456")
    public R<Boolean> resetPassword(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("id").toString());
        return R.status(service.resetPassword(userId));
    }

//    @PostMapping("/assignRoles")
//    @ApiOperationSupport(order = 8)
//    @Operation(summary = "分配角色", description = "传入userId和roleIds列表")
//    public R<Boolean> assignRoles(@RequestBody Map<String, Object> params) {
//        Long userId = Long.valueOf(params.get("userId").toString());
//        @SuppressWarnings("unchecked")
//        List<Integer> rawIds = (List<Integer>) params.get("roleIds");
//        List<Long> roleIds = rawIds.stream().map(Long::valueOf).collect(java.util.stream.Collectors.toList());
//        return R.status(service.assignRoles(userId, roleIds));
//    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 9)
    @Operation(summary = "删除", description = "传入主键集合")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}