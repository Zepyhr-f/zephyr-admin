package com.zephyr.rule.pojo.vo;


import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


/**
* 客户关联人信息实体类
*
* @author zephyr
* @since 2025-10-13
*/
@Data
@Schema(description = "客户关联人信息")
public class AmlPartyAffltVO implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键id")
    Long id;

    /**
    * 数据日期
    */
    @Schema(description = "数据日期")
    private String rq;

    /**
    * 客户号
    */
    @Schema(description = "客户号")
    private String csnm;

    /**
    * 主合同编号
    */
    @Schema(description = "主合同编号")
    private String contNo;

    /**
    * 关联人类型
    */
    @Schema(description = "关联人类型")
    private String guarTy;

    /**
    * 关联人证件类型
    */
    @Schema(description = "关联人证件类型")
    private String guarCredType;

    /**
    * 关联人证件号码
    */
    @Schema(description = "关联人证件号码")
    private String guarCredNo;

    /**
    * 关联人名称
    */
    @Schema(description = "关联人名称")
    private String guarName;

    /**
    * 关联人联系方式
    */
    @Schema(description = "关联人联系方式")
    private String guarCi;

    /**
    * 关联人联系地址
    */
    @Schema(description = "关联人联系地址")
    private String guarAddr;

    /**
    * 主键ID
    */
    @Schema(description = "主键ID")
    private String affltSq;

}
