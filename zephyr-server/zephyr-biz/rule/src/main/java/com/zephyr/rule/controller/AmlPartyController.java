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

import com.zephyr.rule.pojo.entity.AmlParty;
import com.zephyr.rule.service.IAmlPartyService;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.core.tool.api.R;
import com.zephyr.rule.pojo.vo.AmlPartyVO;
import com.zephyr.rule.wrapper.AmlPartyWrapper;

/**
* 当事人信息控制器
*
* @author zephyr
* @since 2025-10-13
*/
@AllArgsConstructor
@RestController
@RequestMapping("/amlparty")
@Tag(name = "当事人信息", description = "当事人信息相关接口")
public class AmlPartyController {

    private final IAmlPartyService service;

    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "详情", description = "传入id")
    public R<AmlPartyVO> detail(@RequestParam("id") Long id) {
        AmlParty amlparty = service.getById(id);
        return R.data(AmlPartyWrapper.build().entityVO(amlparty));
    }

    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分页", description = "传入查询条件")
    public R<IPage<AmlPartyVO>>  list(
            @ParameterObject PageQuery<AmlParty> query,
            @ParameterObject AmlParty amlparty ) {
        IPage<AmlParty> pages = service.page(query.getPage(), new QueryWrapper<>(amlparty));
        return R.data(AmlPartyWrapper.build().pageVO(pages));
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增", description = "传入client")
    public R save(@Valid @RequestBody AmlParty amlparty) {
        return R.status(service.save(amlparty));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "修改", description = "传入实体对象")
    public R update(@Valid @RequestBody AmlParty amlparty) {
        return R.status(service.updateById(amlparty));
    }

    @PostMapping("/submit")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "新增或修改", description = "传入实体对象")
    public R submit(@Valid @RequestBody AmlParty amlparty) {
        return R.status(service.saveOrUpdate(amlparty));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 6)
    @Operation(summary = "删除", description = "传入主键集合")
    public R remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}