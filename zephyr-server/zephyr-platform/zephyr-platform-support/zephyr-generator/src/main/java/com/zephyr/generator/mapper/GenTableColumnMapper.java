package com.zephyr.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.generator.pojo.entity.GenTableColumn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface GenTableColumnMapper extends BaseMapper<GenTableColumn> {
    @Select("select column_name, (case when (is_nullable = 'no' && column_key != 'PRI') then '1' else '0' end) as is_required, " +
            "(case when column_key = 'PRI' then '1' else '0' end) as is_pk, sort_in_temp as sort, column_comment, " +
            "(case when extra = 'auto_increment' then '1' else '0' end) as is_increment, column_type " +
            "from (select column_name, is_nullable, column_key, column_comment, extra, column_type, ordinal_position as sort_in_temp " +
            "from information_schema.columns where table_schema = (select database()) and table_name = #{tableName}) t " +
            "order by sort")
    List<GenTableColumn> selectDbTableColumnsByName(String tableName);
}
