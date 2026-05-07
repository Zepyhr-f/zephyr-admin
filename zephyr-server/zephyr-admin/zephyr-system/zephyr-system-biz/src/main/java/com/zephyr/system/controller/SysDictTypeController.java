package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.system.pojo.entity.SysDictType;
import com.zephyr.system.service.ISysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 字典类型控制器
 *
 * @author zephyr
 */
@RestController
@RequestMapping("/dict/type")
@AllArgsConstructor
@Tag(name = "字典类型管理", description = "字典类型管理")
public class SysDictTypeController {

    private final ISysDictTypeService dictTypeService;

    @GetMapping("/page")
    @Operation(summary = "分页")
    public R<IPage<SysDictType>> page(@ParameterObject PageQuery<SysDictType> query, 
                                      @ParameterObject SysDictType dictType) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<SysDictType>()
                .like(StringUtils.hasText(dictType.getDictName()), SysDictType::getDictName, dictType.getDictName())
                .like(StringUtils.hasText(dictType.getDictType()), SysDictType::getDictType, dictType.getDictType())
                .eq(dictType.getStatus() != null, SysDictType::getStatus, dictType.getStatus());
        return R.data(dictTypeService.page(query.getPage(), wrapper));
    }

    @PostMapping("/submit")
    @Operation(summary = "新增或修改")
    public R<Boolean> submit(@Valid @RequestBody SysDictType dictType) {
        return R.status(dictTypeService.saveOrUpdate(dictType));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(dictTypeService.removeByIds(ids));
    }

    @PostMapping("/clear-cache")
    @Operation(summary = "清空缓存")
    public R<Void> clearCache() {
        dictTypeService.clearCache();
        return R.success("刷新成功");
    }
}
