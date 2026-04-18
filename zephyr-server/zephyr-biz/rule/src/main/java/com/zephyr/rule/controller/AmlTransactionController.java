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

import com.zephyr.rule.pojo.entity.AmlTransaction;
import com.zephyr.rule.service.IAmlTransactionService;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.core.tool.api.R;
import com.zephyr.rule.pojo.vo.AmlTransactionVO;
import com.zephyr.rule.wrapper.AmlTransactionWrapper;

/**
* 交易流水控制器
*
* @author zephyr
* @since 2025-10-13
*/
@AllArgsConstructor
@RestController
@RequestMapping("/amltransaction")
@Tag(name = "交易流水", description = "交易流水相关接口")
public class AmlTransactionController {

    private final IAmlTransactionService service;

    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "详情", description = "传入id")
    public R<AmlTransactionVO> detail(@RequestParam("id") Long id) {
        AmlTransaction amltransaction = service.getById(id);
        return R.data(AmlTransactionWrapper.build().entityVO(amltransaction));
    }

    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分页", description = "传入查询条件")
    public R<IPage<AmlTransactionVO>>  list(
            @ParameterObject PageQuery<AmlTransaction> query,
            @ParameterObject AmlTransaction amltransaction ) {
        IPage<AmlTransaction> pages = service.page(query.getPage(), new QueryWrapper<>(amltransaction));
        return R.data(AmlTransactionWrapper.build().pageVO(pages));
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增", description = "传入client")
    public R save(@Valid @RequestBody AmlTransaction amltransaction) {
        return R.status(service.save(amltransaction));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "修改", description = "传入实体对象")
    public R update(@Valid @RequestBody AmlTransaction amltransaction) {
        return R.status(service.updateById(amltransaction));
    }

    @PostMapping("/submit")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "新增或修改", description = "传入实体对象")
    public R submit(@Valid @RequestBody AmlTransaction amltransaction) {
        return R.status(service.saveOrUpdate(amltransaction));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 6)
    @Operation(summary = "删除", description = "传入主键集合")
    public R remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}