package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.system.pojo.entity.SysFile;
import com.zephyr.system.service.ISysFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 文件记录控制器
 *
 * @author zephyr
 */
@RestController
@RequestMapping("/file")
@AllArgsConstructor
@Tag(name = "文件管理", description = "文件管理相关接口")
public class SysFileController {

    private final ISysFileService fileService;

    @GetMapping("/page")
    @Operation(summary = "分页")
    public R<IPage<SysFile>> page(@ParameterObject PageQuery<SysFile> query, 
                                  @ParameterObject SysFile file) {
        LambdaQueryWrapper<SysFile> wrapper = new LambdaQueryWrapper<SysFile>()
                .like(StringUtils.hasText(file.getFileName()), SysFile::getFileName, file.getFileName())
                .eq(StringUtils.hasText(file.getStoreType()), SysFile::getStoreType, file.getStoreType());
        return R.data(fileService.page(query.getPage(), wrapper));
    }

    @PostMapping("/remove")
    @Operation(summary = "逻辑删除")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(fileService.removeByIds(ids));
    }
}
