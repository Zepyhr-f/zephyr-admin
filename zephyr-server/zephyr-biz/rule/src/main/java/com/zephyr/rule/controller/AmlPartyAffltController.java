package com.zephyr.rule.controller;


import java.util.List;

import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.zephyr.rule.pojo.entity.AmlPartyAfflt;
import com.zephyr.rule.service.IAmlPartyAffltService;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.core.tool.api.R;
import com.zephyr.rule.pojo.vo.AmlPartyAffltVO;
import com.zephyr.rule.wrapper.AmlPartyAffltWrapper;

/**
* 客户关联人信息控制器
*
* @author zephyr
* @since 2025-10-13
*/
@AllArgsConstructor
@RestController
@RequestMapping("/amlpartyafflt")
@Tag(name = "客户关联人信息", description = "客户关联人信息相关接口")
public class AmlPartyAffltController {

    private final IAmlPartyAffltService service;

    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "详情", description = "传入id")
    public R<AmlPartyAffltVO> detail(@RequestParam("id") Long id) {
        AmlPartyAfflt amlpartyafflt = service.getById(id);
        return R.data(AmlPartyAffltWrapper.build().entityVO(amlpartyafflt));
    }

    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分页", description = "传入查询条件")
    public R<IPage<AmlPartyAffltVO>>  list(
            @ParameterObject PageQuery<AmlPartyAfflt> query,
            @ParameterObject AmlPartyAfflt amlpartyafflt ) {
        IPage<AmlPartyAfflt> pages = service.page(query.getPage(), new QueryWrapper<>(amlpartyafflt));
        return R.data(AmlPartyAffltWrapper.build().pageVO(pages));
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增", description = "传入client")
    public R save(@Valid @RequestBody AmlPartyAfflt amlpartyafflt) {
        return R.status(service.save(amlpartyafflt));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "修改", description = "传入实体对象")
    public R update(@Valid @RequestBody AmlPartyAfflt amlpartyafflt) {
        return R.status(service.updateById(amlpartyafflt));
    }

    @PostMapping("/submit")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "新增或修改", description = "传入实体对象")
    public R submit(@Valid @RequestBody AmlPartyAfflt amlpartyafflt) {
        return R.status(service.saveOrUpdate(amlpartyafflt));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 6)
    @Operation(summary = "删除", description = "传入主键集合")
    public R remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}