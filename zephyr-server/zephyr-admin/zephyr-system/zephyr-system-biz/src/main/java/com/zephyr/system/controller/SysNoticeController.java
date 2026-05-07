package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.system.pojo.entity.SysNotice;
import com.zephyr.system.service.ISysNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 通知公告控制器
 *
 * @author zephyr
 */
@RestController
@RequestMapping("/notice")
@AllArgsConstructor
@Tag(name = "通知公告管理", description = "通知公告管理")
public class SysNoticeController {

    private final ISysNoticeService noticeService;

    @GetMapping("/page")
    @Operation(summary = "分页")
    public R<IPage<SysNotice>> page(@ParameterObject PageQuery<SysNotice> query, 
                                    @ParameterObject SysNotice notice) {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<SysNotice>()
                .like(StringUtils.hasText(notice.getNoticeTitle()), SysNotice::getNoticeTitle, notice.getNoticeTitle())
                .eq(notice.getNoticeType() != null, SysNotice::getNoticeType, notice.getNoticeType())
                .eq(notice.getStatus() != null, SysNotice::getStatus, notice.getStatus());
        return R.data(noticeService.page(query.getPage(), wrapper));
    }

    @PostMapping("/submit")
    @Operation(summary = "新增或修改")
    public R<Boolean> submit(@Valid @RequestBody SysNotice notice) {
        return R.status(noticeService.saveOrUpdate(notice));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(noticeService.removeByIds(ids));
    }
}
