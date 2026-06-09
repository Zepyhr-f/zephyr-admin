package com.zephyr.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.zephyr.mp.base.TreeEntity;

import java.util.Collection;
import java.util.List;

/**
 * 树形实体 Mapper 接口
 * <p>
 * 适用于继承自 {@link TreeEntity} 的实体，提供基于 <b>parentCode</b> 字段的树形结构便捷查询方法。
 * 同时继承 {@link ZCodeMapper} 的全部能力（selectByCode、existByCode 等），
 * 业务 Mapper 只需继承此接口，即可获得完整的树形 + 编码查询能力，无需编写简单 SQL。
 *
 * <p>示例：
 * <pre>
 * public interface DeptMapper extends ZTreeMapper&lt;Dept&gt; {
 *     // 无需声明任何方法，直接拥有树形查询能力
 * }
 * </pre>
 *
 * @param <T> 实体类型，必须继承 {@link TreeEntity}
 * @author Zephyr
 * @since 2025-09-23
 */
public interface ZTreeMapper<T extends TreeEntity> extends ZCodeMapper<T> {

    /**
     * 获取 parentCode 字段的 Lambda 函数引用，用于类型安全的条件构造
     */
    default SFunction<T, String> parentCodeFunc() {
        return t -> t.getParentCode();
    }

    /**
     * 根据父编码查询直接子节点列表
     *
     * @param parentCode 父编码
     * @return 子节点列表
     */
    default List<T> selectByParentCode(String parentCode) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(parentCodeFunc(), parentCode);
        return selectList(wrapper);
    }

    /**
     * 根据父编码列表批量查询子节点列表
     *
     * @param parentCodes 父编码集合
     * @return 子节点列表
     */
    default List<T> selectByParentCodes(Collection<String> parentCodes) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(parentCodeFunc(), parentCodes);
        return selectList(wrapper);
    }

    /**
     * 查询根节点列表（parentCode 为 null）
     *
     * @return 根节点列表
     */
    default List<T> selectRootList() {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(parentCodeFunc());
        return selectList(wrapper);
    }

    /**
     * 判断指定父编码下是否存在子节点
     *
     * @param parentCode 父编码
     * @return true-存在子节点；false-不存在
     */
    default boolean hasChildren(String parentCode) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(parentCodeFunc(), parentCode);
        return selectCount(wrapper) > 0;
    }
}
