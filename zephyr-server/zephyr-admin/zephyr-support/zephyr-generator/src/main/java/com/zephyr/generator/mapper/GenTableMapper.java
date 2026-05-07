package com.zephyr.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.generator.pojo.entity.GenTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface GenTableMapper extends BaseMapper<GenTable> {
    @Select("select table_name, table_comment, create_time, update_time from information_schema.tables " +
            "where table_schema = (select database()) " +
            "and table_name NOT LIKE 'gen_%' " +
            "and table_name NOT IN (select table_name from gen_table)")
    List<GenTable> selectDbTableList(GenTable genTable);
}
