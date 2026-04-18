package ${packageName}.controller;


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

import ${packageName}.pojo.entity.${className};
import ${packageName}.service.I${className}Service;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.core.tool.api.R;
import ${packageName}.pojo.vo.${className}VO;
import ${packageName}.wrapper.${className}Wrapper;

<#assign IPageRes = "R<IPage<" + className + ">>">
/**
* ${comment!}控制器
*
* @author ${author!}
* @since ${date!}
*/
@AllArgsConstructor
@RestController
@RequestMapping("/${className?lower_case}")
@Tag(name = "${comment!}", description = "${comment!}相关接口")
public class ${className}Controller {

    private final I${className}Service service;

    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "详情", description = "传入id")
    public R<${className}VO> detail(@RequestParam("id") Long id) {
        ${className} ${className?lower_case} = service.getById(id);
        return R.data(${className}Wrapper.build().entityVO(${className?lower_case}));
    }

    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分页", description = "传入查询条件")
    public R<IPage<${className}VO>>  list(
            @ParameterObject PageQuery<${className}> query,
            @ParameterObject ${className} ${className?lower_case} ) {
        IPage<${className}> pages = service.page(query.getPage(), new QueryWrapper<>(${className?lower_case}));
        return R.data(${className}Wrapper.build().pageVO(pages));
    }

    @PostMapping("/save")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "新增", description = "传入client")
    public R save(@Valid @RequestBody ${className} ${className?lower_case}) {
        return R.status(service.save(${className?lower_case}));
    }

    @PostMapping("/update")
    @ApiOperationSupport(order = 4)
    @Operation(summary = "修改", description = "传入实体对象")
    public R update(@Valid @RequestBody ${className} ${className?lower_case}) {
        return R.status(service.updateById(${className?lower_case}));
    }

    @PostMapping("/submit")
    @ApiOperationSupport(order = 5)
    @Operation(summary = "新增或修改", description = "传入实体对象")
    public R submit(@Valid @RequestBody ${className} ${className?lower_case}) {
        return R.status(service.saveOrUpdate(${className?lower_case}));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 6)
    @Operation(summary = "删除", description = "传入主键集合")
    public R remove(@RequestBody List<Long> ids) {
        return R.status(service.removeByIds(ids));
    }
}