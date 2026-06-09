package com.zephyr.system.pojo.dto;

import com.zephyr.system.pojo.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户 DTO
 *
 * @author zephyr
 * @since 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户传输对象")
public class UserDTO extends User {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID集合
     */
    @Schema(description = "角色ID集合")
    private List<Long> roleIds;

}
