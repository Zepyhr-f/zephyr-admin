package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件记录表
 *
 * @author zephyr
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file")
@Schema(description = "文件记录")
public class SysFile extends BaseEntity {

    @Schema(description = "原始文件名")
    private String fileName;

    @Schema(description = "存储相对路径")
    private String filePath;

    @Schema(description = "访问全路径")
    private String fileUrl;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件后缀")
    private String fileSuffix;

    @Schema(description = "存储类型 (local/oss/minio)")
    private String storeType;
}
