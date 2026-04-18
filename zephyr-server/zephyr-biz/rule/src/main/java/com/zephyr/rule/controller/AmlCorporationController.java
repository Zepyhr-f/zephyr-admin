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

import com.zephyr.rule.pojo.entity.AmlCorporation;
import com.zephyr.rule.service.IAmlCorporationService;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.core.tool.api.R;
import com.zephyr.rule.pojo.vo.AmlCorporationVO;
import com.zephyr.rule.wrapper.AmlCorporationWrapper;

/**
* 公司信息控制器
*
* @author zephyr
* @since 2025-10-13
*/
@AllArgsConstructor
@RestController
@RequestMapping("/amlcorporation")
@Tag(name = "公司信息", description = "公司信息相关接口")
public class AmlCorporationController {

    private final IAmlCorporationService service;

    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "详情", description = "传入id")
    public R<AmlCorporationVO> detail(@RequestParam("id") Long id) {
        AmlCorporation amlcorporation = service.getById(id);
        return R.data(AmlCorporationWrapper.build().entityVO(amlcorporation));
    }

    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分页", description = "传入查询条件")
    public R<IPage<AmlCorporationVO>>  list(
            @ParameterObject PageQuery<AmlCorporation> query,
            @ParameterObject AmlCorporation amlcorporation ) {
        IPage<AmlCorporation> pages = service.page(query.getPage(), new QueryWrapper<>(amlcorporation));
        return R.data(AmlCorporationWrapper.build().pageVO(pages));
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增", description = "传入client")
    public R save(@Valid @RequestBody AmlCorporation amlcorporation) {
        return R.status(service.save(amlcorporation));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "修改", description = "传入实体对象")
    public R update(@Valid @RequestBody AmlCorporation amlcorporation) {
        return R.status(service.updateById(amlcorporation));
    }

    @PostMapping("/submit")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "新增或修改", description = "传入实体对象")
    public R submit(@Valid @RequestBody AmlCorporation amlcorporation) {
        return R.status(service.saveOrUpdate(amlcorporation));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 6)
    @Operation(summary = "删除", description = "传入主键集合")
    public R remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}