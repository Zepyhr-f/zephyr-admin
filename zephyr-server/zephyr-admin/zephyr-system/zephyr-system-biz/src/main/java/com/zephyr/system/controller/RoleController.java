package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.vo.RoleVO;
import com.zephyr.system.service.IRoleService;
import com.zephyr.system.wrapper.RoleWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色管理控制器
 *
 * @author zephyr
 */
@AllArgsConstructor
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色管理相关接口")
public class RoleController {

    private final IRoleService service;

    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "详情", description = "传入id")
    public R<RoleVO> detail(@RequestParam("id") Long id) {
        Role role = service.getById(id);
        return R.data(RoleWrapper.build().entityVO(role));
    }

    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分页列表", description = "传入查询条件")
    public R<IPage<RoleVO>> list(
            @ParameterObject PageQuery<Role> query,
            @ParameterObject Role role) {
        IPage<Role> pages = service.page(query.getPage(),
                new LambdaQueryWrapper<Role>()
                        .like(role.getRoleName() != null, Role::getRoleName, role.getRoleName())
                        .eq(role.getStatus() != null, Role::getStatus, role.getStatus())
                        .orderByAsc(Role::getOrderNum));
        return R.data(RoleWrapper.build().pageVO(pages));
    }

    @GetMapping("/all")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "全部启用角色", description = "用于分配角色下拉选择")
    public R<List<Role>> all() {
        return R.data(service.listAllEnabled());
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "新增", description = "传入角色实体")
    public R<Boolean> save(@Valid @RequestBody Role role) {
        return R.status(service.save(role));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "修改", description = "传入角色实体")
    public R<Boolean> update(@Valid @RequestBody Role role) {
        return R.status(service.updateById(role));
    }

    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @Operation(summary = "新增或修改", description = "传入角色实体")
    public R<Boolean> submit(@Valid @RequestBody Role role) {
        return R.status(service.saveOrUpdate(role));
    }

    @PostMapping("/updateStatus")
    @ApiOperationSupport(order = 7)
    @Operation(summary = "更新状态", description = "传入roleId和status")
    public R<Boolean> updateStatus(@RequestBody Map<String, Object> params) {
        Long roleId = Long.valueOf(params.get("id").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        return R.status(service.updateStatus(roleId, status));
    }

    @PostMapping("/assignMenus")
    @ApiOperationSupport(order = 8)
    @Operation(summary = "权限授权", description = "传入roleCode和menuCodes列表")
    public R<Boolean> assignMenus(@RequestBody Map<String, Object> params) {
        String roleCode = params.get("roleCode").toString();
        @SuppressWarnings("unchecked")
        List<String> menuCodes = (List<String>) params.get("menuCodes");
        return R.status(service.assignMenus(roleCode, menuCodes));
    }

    @GetMapping("/menuCodes/{roleCode}")
    @ApiOperationSupport(order = 9)
    @Operation(summary = "角色已有菜单编码", description = "传入roleCode")
    public R<List<String>> menuCodes(@PathVariable String roleCode) {
        return R.data(service.getMenuCodesByRoleCode(roleCode));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 10)
    @Operation(summary = "删除", description = "传入主键集合")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}
