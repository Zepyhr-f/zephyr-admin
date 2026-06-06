package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.CodeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位信息实体
 *
 * @author zephyr
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zephyr_sys_post")
@Schema(description = "岗位")
public class Post extends CodeEntity {

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "岗位状态（1=正常 0=停用）")
    private Integer status;
}
