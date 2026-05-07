package com.zephyr.system.controller;

import com.zephyr.core.tool.api.R;
import com.zephyr.system.pojo.entity.Menu;
import com.zephyr.system.pojo.vo.MenuVO;
import com.zephyr.system.service.IMenuService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 *
 * @author zephyr
 */
@AllArgsConstructor
@RestController
@RequestMapping("/menu")
@Tag(name = "菜单管理", description = "菜单/权限规则相关接口")
public class MenuController {

    private final IMenuService service;

    @GetMapping("/tree")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "菜单树", description = "查询全部菜单树形结构")
    public R<List<MenuVO>> tree() {
        return R.data(service.listTree());
    }

    @GetMapping("/detail")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "详情", description = "传入id")
    public R<Menu> detail(@RequestParam("id") Long id) {
        return R.data(service.getById(id));
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增", description = "传入菜单实体")
    public R<Boolean> save(@Valid @RequestBody Menu menu) {
        return R.status(service.save(menu));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "修改", description = "传入菜单实体")
    public R<Boolean> update(@Valid @RequestBody Menu menu) {
        return R.status(service.updateById(menu));
    }

    @PostMapping("/submit")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "新增或修改", description = "传入菜单实体")
    public R<Boolean> submit(@Valid @RequestBody Menu menu) {
        return R.status(service.saveOrUpdate(menu));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 6)
    @Operation(summary = "删除", description = "传入主键集合")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}