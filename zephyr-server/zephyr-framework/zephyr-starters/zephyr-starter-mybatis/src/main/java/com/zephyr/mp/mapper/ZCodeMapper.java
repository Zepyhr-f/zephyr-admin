package com.zephyr.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.zephyr.mp.base.CodeEntity;

import java.util.Collection;
import java.util.List;

/**
 * 编码实体 Mapper 接口
 * <p>
 * 适用于继承自 {@link CodeEntity} 的实体，提供基于 <b>code</b> 字段的便捷查询方法。
 * 业务 Mapper 只需继承此接口，即可直接通过 code 进行查询，无需编写简单 SQL。
 *
 * <p>示例：
 * <pre>
 * public interface RoleMapper extends ZCodeMapper&lt;Role&gt; {
 *     // 无需声明任何方法，直接拥有 selectByCode / existByCode 等能力
 * }
 * </pre>
 *
 * @param <T> 实体类型，必须继承 {@link CodeEntity}
 * @author Zephyr
 * @since 2025-09-23
 */
public interface ZCodeMapper<T extends CodeEntity> extends ZBaseMapper<T> {

    /**
     * 获取 code 字段的 Lambda 函数引用，用于类型安全的条件构造
     */
    default SFunction<T, String> codeFunc() {
        return t -> t.getCode();
    }

    /**
     * 根据唯一编码查询实体
     *
     * @param code 唯一编码
     * @return 实体对象，不存在则返回 null
     */
    default T selectByCode(String code) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(codeFunc(), code);
        return selectOne(wrapper);
    }

    /**
     * 根据编码列表批量查询实体
     *
     * @param codes 编码集合
     * @return 实体列表
     */
    default List<T> selectByCodes(Collection<String> codes) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(codeFunc(), codes);
        return selectList(wrapper);
    }

    /**
     * 判断指定编码是否存在
     *
     * @param code 唯一编码
     * @return true-存在；false-不存在
     */
    default boolean existByCode(String code) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(codeFunc(), code);
        return selectCount(wrapper) > 0;
    }
}
