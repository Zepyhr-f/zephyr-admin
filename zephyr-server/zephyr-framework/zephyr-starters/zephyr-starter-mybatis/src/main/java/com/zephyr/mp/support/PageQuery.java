package com.zephyr.mp.support;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分页查询
 *
 * @author Zephyr
 * @since 2025-09-20
 */
@Data
@Schema(description = "分页查询条件")
public class PageQuery<T> {
    @Schema(description = "当前页")
    private long current = 1L;
    @Schema(description = "每页的数量")
    private long size = 10L;

    @Schema(description = "排序字段")
    private String orderBy;

    @Schema(description = "是否升序")
    private boolean asc = true;

    public Page<T> getPage() {
        Page<T> page = new Page<>(current, size);
        if (orderBy != null && !orderBy.isEmpty()) {
            OrderItem orderItem = asc ? OrderItem.asc(orderBy) : OrderItem.desc(orderBy);
            page.setOrders(List.of(orderItem));
        }
        return page;
    }
}