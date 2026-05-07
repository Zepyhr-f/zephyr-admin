package com.zephyr.system.controller;

import com.zephyr.core.tool.api.R;
import com.zephyr.system.pojo.entity.Dept;
import com.zephyr.system.pojo.vo.DeptVO;
import com.zephyr.system.service.IDeptService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 *
 * @author zephyr
 */
@AllArgsConstructor
@RestController
@RequestMapping("/dept")
@Tag(name = "部门管理", description = "部门管理相关接口")
public class DeptController {

    private final IDeptService service;

    @GetMapping("/tree")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "部门树", description = "查询全部部门树形结构")
    public R<List<DeptVO>> tree() {
        return R.data(service.listTree());
    }

    @GetMapping("/detail")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "详情", description = "传入id")
    public R<Dept> detail(@RequestParam("id") Long id) {
        return R.data(service.getById(id));
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增", description = "传入部门实体")
    public R<Boolean> save(@Valid @RequestBody Dept dept) {
        return R.status(service.save(dept));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "修改", description = "传入部门实体")
    public R<Boolean> update(@Valid @RequestBody Dept dept) {
        return R.status(service.updateById(dept));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "删除", description = "传入主键集合")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}
