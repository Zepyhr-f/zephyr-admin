package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.system.pojo.entity.SysDictData;
import com.zephyr.system.service.ISysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 字典数据控制器
 *
 * @author zephyr
 */
@RestController
@RequestMapping("/dict/data")
@AllArgsConstructor
@Tag(name = "字典数据管理", description = "字典数据管理")
public class SysDictDataController {

    private final ISysDictDataService dictDataService;

    @GetMapping("/page")
    @Operation(summary = "分页")
    public R<IPage<SysDictData>> page(@ParameterObject PageQuery<SysDictData> query, 
                                      @ParameterObject SysDictData dictData) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<SysDictData>()
                .eq(StringUtils.hasText(dictData.getDictType()), SysDictData::getDictType, dictData.getDictType())
                .like(StringUtils.hasText(dictData.getDictLabel()), SysDictData::getDictLabel, dictData.getDictLabel())
                .eq(dictData.getStatus() != null, SysDictData::getStatus, dictData.getStatus())
                .orderByAsc(SysDictData::getDictSort);
        return R.data(dictDataService.page(query.getPage(), wrapper));
    }

    @GetMapping("/type")
    @Operation(summary = "按类型获取数据")
    public R<List<SysDictData>> getByType(@RequestParam("dictType") String dictType) {
        return R.data(dictDataService.selectDictDataByType(dictType));
    }

    @PostMapping("/submit")
    @Operation(summary = "新增或修改")
    public R<Boolean> submit(@Valid @RequestBody SysDictData dictData) {
        return R.status(dictDataService.saveOrUpdate(dictData));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(dictDataService.removeByIds(ids));
    }
}
