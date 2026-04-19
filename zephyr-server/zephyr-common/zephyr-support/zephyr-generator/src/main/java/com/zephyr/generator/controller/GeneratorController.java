package com.zephyr.generator.controller;


import com.zephyr.core.tool.api.R;
import com.zephyr.generator.pojo.dto.GenInfo;
import com.zephyr.generator.service.impl.GeneratorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 代码生成控制器
 *
 * @author Zephyr
 * @since 2025-09-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/generator")
@Tag(name = "代码生成器", description = "提供代码生成相关接口")
public class GeneratorController {

    private final GeneratorServiceImpl generatorService;

    @PostMapping("/run")
    @Operation(summary = "生成代码", description = "根据表名和模块名生成代码")
    public R<String> run(@RequestBody GenInfo genInfo) {
        return R.success("""
                代码生成成功！
                代码生成路径为：%s
                """, generatorService.generateCode(genInfo));
    }
}