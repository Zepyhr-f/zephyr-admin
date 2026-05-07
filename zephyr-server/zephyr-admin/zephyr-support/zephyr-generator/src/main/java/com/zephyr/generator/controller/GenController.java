package com.zephyr.generator.controller;

import com.zephyr.core.tool.api.R;
import com.zephyr.generator.pojo.entity.GenTable;
import com.zephyr.generator.service.IGenTableService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/gen")
public class GenController {

    private final IGenTableService genTableService;

    @GetMapping("/list")
    public Map<String, Object> list(GenTable genTable) {
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        return Map.of("rows", list, "total", (long) list.size());
    }

    @GetMapping("/db/list")
    public Map<String, Object> dbList(GenTable genTable) {
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return Map.of("rows", list, "total", (long) list.size());
    }

    @PostMapping("/importTable")
    public R<Void> importTable(@RequestParam("tables") String tables) {
        genTableService.importTable(tables.split(","));
        return R.success("导入成功");
    }

    @GetMapping("/{tableId}")
    public Map<String, Object> getInfo(@PathVariable("tableId") Long tableId) {
        GenTable table = genTableService.selectGenTableById(tableId);
        return Map.of("data", Map.of("info", table));
    }

    @PutMapping
    public R<Void> edit(@RequestBody GenTable genTable) {
        genTableService.updateGenTable(genTable);
        return R.success("修改成功");
    }

    @DeleteMapping("/{tableIds}")
    public R<Void> remove(@PathVariable("tableIds") Long[] tableIds) {
        genTableService.deleteGenTableByIds(tableIds);
        return R.success("删除成功");
    }

    @GetMapping("/preview/{tableId}")
    public Map<String, Object> preview(@PathVariable("tableId") Long tableId) {
        return Map.of("data", genTableService.previewCode(tableId));
    }

    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genTableService.downloadCode(tableName);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"zephyr.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

    @GetMapping("/synchDb/{tableName}")
    public R<Void> synchDb(@PathVariable("tableName") String tableName) {
        genTableService.synchDb(tableName);
        return R.success("同步成功");
    }
}
