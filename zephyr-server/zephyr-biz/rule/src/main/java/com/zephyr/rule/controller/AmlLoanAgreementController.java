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

import com.zephyr.rule.pojo.entity.AmlLoanAgreement;
import com.zephyr.rule.service.IAmlLoanAgreementService;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.core.tool.api.R;
import com.zephyr.rule.pojo.vo.AmlLoanAgreementVO;
import com.zephyr.rule.wrapper.AmlLoanAgreementWrapper;

/**
* 租赁协议信息控制器
*
* @author zephyr
* @since 2025-10-13
*/
@AllArgsConstructor
@RestController
@RequestMapping("/amlloanagreement")
@Tag(name = "租赁协议信息", description = "租赁协议信息相关接口")
public class AmlLoanAgreementController {

    private final IAmlLoanAgreementService service;

    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "详情", description = "传入id")
    public R<AmlLoanAgreementVO> detail(@RequestParam("id") Long id) {
        AmlLoanAgreement amlloanagreement = service.getById(id);
        return R.data(AmlLoanAgreementWrapper.build().entityVO(amlloanagreement));
    }

    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分页", description = "传入查询条件")
    public R<IPage<AmlLoanAgreementVO>>  list(
            @ParameterObject PageQuery<AmlLoanAgreement> query,
            @ParameterObject AmlLoanAgreement amlloanagreement ) {
        IPage<AmlLoanAgreement> pages = service.page(query.getPage(), new QueryWrapper<>(amlloanagreement));
        return R.data(AmlLoanAgreementWrapper.build().pageVO(pages));
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增", description = "传入client")
    public R save(@Valid @RequestBody AmlLoanAgreement amlloanagreement) {
        return R.status(service.save(amlloanagreement));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "修改", description = "传入实体对象")
    public R update(@Valid @RequestBody AmlLoanAgreement amlloanagreement) {
        return R.status(service.updateById(amlloanagreement));
    }

    @PostMapping("/submit")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "新增或修改", description = "传入实体对象")
    public R submit(@Valid @RequestBody AmlLoanAgreement amlloanagreement) {
        return R.status(service.saveOrUpdate(amlloanagreement));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 6)
    @Operation(summary = "删除", description = "传入主键集合")
    public R remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}