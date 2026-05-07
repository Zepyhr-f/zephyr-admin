package com.zephyr.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.generator.mapper.GenTableColumnMapper;
import com.zephyr.generator.mapper.GenTableMapper;
import com.zephyr.generator.pojo.entity.GenTable;
import com.zephyr.generator.pojo.entity.GenTableColumn;
import com.zephyr.generator.service.IGenTableService;
import com.zephyr.generator.util.TemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements IGenTableService {

    private final GenTableColumnMapper genTableColumnMapper;

    @Override
    public List<GenTable> selectGenTableList(GenTable genTable) {
        return baseMapper.selectList(new LambdaQueryWrapper<GenTable>()
                .like(genTable.getTableName() != null, GenTable::getTableName, genTable.getTableName())
                .orderByDesc(GenTable::getCreateTime));
    }

    @Override
    public List<GenTable> selectDbTableList(GenTable genTable) {
        return baseMapper.selectDbTableList(genTable);
    }

    @Override
    @Transactional
    public void importTable(String[] tableNames) {
        for (String tableName : tableNames) {
            GenTable table = new GenTable();
            table.setTableName(tableName);
            table.setTableComment(tableName);
            table.setClassName(convertClassName(tableName));
            table.setPackageName("com.zephyr.biz");
            table.setModuleName("system");
            table.setBusinessName(tableName);
            table.setFunctionName(tableName);
            table.setFunctionAuthor("Zephyr");
            table.setCreateTime(new Date());
            save(table);

            List<GenTableColumn> columns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
            for (GenTableColumn column : columns) {
                column.setTableId(table.getTableId());
                column.setCreateTime(new Date());
                column.setJavaField(convertJavaField(column.getColumnName()));
                column.setJavaType("String"); // 简便起见，默认String
                genTableColumnMapper.insert(column);
            }
        }
    }

    @Override
    public GenTable selectGenTableById(Long id) {
        GenTable table = getById(id);
        table.setColumns(genTableColumnMapper.selectList(new LambdaQueryWrapper<GenTableColumn>()
                .eq(GenTableColumn::getTableId, id)
                .orderByAsc(GenTableColumn::getSort)));
        return table;
    }

    @Override
    @Transactional
    public void updateGenTable(GenTable genTable) {
        updateById(genTable);
        // 更新列信息可在此扩展
    }

    @Override
    @Transactional
    public void deleteGenTableByIds(Long[] tableIds) {
        for (Long tableId : tableIds) {
            removeById(tableId);
            genTableColumnMapper.delete(new LambdaQueryWrapper<GenTableColumn>().eq(GenTableColumn::getTableId, tableId));
        }
    }

    @Override
    public Map<String, String> previewCode(Long tableId) {
        GenTable table = selectGenTableById(tableId);
        Map<String, String> dataMap = new LinkedHashMap<>();
        Configuration cfg = TemplateUtils.createFreemarkerConfig();
        Map<String, Object> context = prepareContext(table);

        String[] templates = {"entity.java.ftl", "mapper.java.ftl", "service.java.ftl", "serviceImpl.java.ftl", "controller.java.ftl"};
        for (String templateName : templates) {
            try (StringWriter sw = new StringWriter()) {
                Template tpl = cfg.getTemplate(templateName);
                tpl.process(context, sw);
                dataMap.put(templateName.replace(".ftl", ""), sw.toString());
            } catch (Exception e) {
                dataMap.put(templateName, "生成失败: " + e.getMessage());
            }
        }
        return dataMap;
    }

    @Override
    public byte[] downloadCode(String tableName) {
        GenTable table = baseMapper.selectOne(new LambdaQueryWrapper<GenTable>().eq(GenTable::getTableName, tableName));
        if (table == null) return new byte[0];
        table = selectGenTableById(table.getTableId());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        
        Map<String, String> codes = previewCode(table.getTableId());
        for (Map.Entry<String, String> entry : codes.entrySet()) {
            try {
                zip.putNextEntry(new ZipEntry(entry.getKey().replace(".", "/") + ".java"));
                zip.write(entry.getValue().getBytes());
                zip.flush();
                zip.closeEntry();
            } catch (Exception e) {
                // Ignore
            }
        }
        try {
            zip.close();
        } catch (Exception e) {}
        return outputStream.toByteArray();
    }

    private Map<String, Object> prepareContext(GenTable table) {
        Map<String, Object> context = new HashMap<>();
        context.put("packageName", table.getPackageName());
        context.put("className", table.getClassName());
        context.put("author", table.getFunctionAuthor());
        context.put("date", new java.util.Date().toString());
        context.put("comment", table.getTableComment());
        context.put("tableName", table.getTableName());
        
        List<Map<String, Object>> fields = new ArrayList<>();
        for (GenTableColumn column : table.getColumns()) {
            Map<String, Object> field = new HashMap<>();
            field.put("name", column.getJavaField());
            field.put("type", convertJavaType(column.getColumnType()));
            field.put("comment", column.getColumnComment());
            field.put("serializeToString", "Long".equals(field.get("type")));
            fields.add(field);
        }
        context.put("fields", fields);
        context.put("baseImports", new ArrayList<>());
        context.put("jarImports", new ArrayList<>());
        return context;
    }

    private String convertJavaType(String columnType) {
        if (columnType == null) return "String";
        String type = columnType.toLowerCase();
        if (type.contains("bigint")) return "Long";
        if (type.contains("int")) return "Integer";
        if (type.contains("date") || type.contains("time")) return "Date";
        if (type.contains("decimal")) return "BigDecimal";
        return "String";
    }

    @Override
    public void synchDb(String tableName) {
        // TODO: 同步数据库结构
    }

    private String convertClassName(String tableName) {
        StringBuilder sb = new StringBuilder();
        for (String s : tableName.split("_")) {
            if (s.length() > 0) {
                sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1));
            }
        }
        return sb.toString();
    }

    private String convertJavaField(String columnName) {
        String[] s = columnName.split("_");
        StringBuilder sb = new StringBuilder(s[0]);
        for (int i = 1; i < s.length; i++) {
            sb.append(Character.toUpperCase(s[i].charAt(0))).append(s[i].substring(1));
        }
        return sb.toString();
    }
}
