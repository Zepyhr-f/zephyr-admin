package com.zephyr.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.generator.pojo.entity.GenTable;
import java.util.List;
import java.util.Map;

public interface IGenTableService extends IService<GenTable> {
    List<GenTable> selectGenTableList(GenTable genTable);
    List<GenTable> selectDbTableList(GenTable genTable);
    void importTable(String[] tableNames);
    GenTable selectGenTableById(Long id);
    void updateGenTable(GenTable genTable);
    void deleteGenTableByIds(Long[] tableIds);
    Map<String, String> previewCode(Long tableId);
    byte[] downloadCode(String tableName);
    void synchDb(String tableName);
}
