package com.zephyr.rule.pojo.vo;


import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


/**
* 收益所有人实体类
*
* @author zephyr
* @since 2025-10-13
*/
@Data
@Schema(description = "收益所有人")
public class AmlBnfInfoVO implements Serializable{

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
    * 客户名称
    */
    @Schema(description = "客户名称")
    private String ctnm;

    /**
    * 受益所有人姓名
    */
    @Schema(description = "受益所有人姓名")
    private String beneNm;

    /**
    * 受益所有人地址
    */
    @Schema(description = "受益所有人地址")
    private String beneAddr;

    /**
    * 受益所有人身份证件种类
    */
    @Schema(description = "受益所有人身份证件种类")
    private String beneTy;

    /**
    * 受益所有人身份证件号码
    */
    @Schema(description = "受益所有人身份证件号码")
    private String beneId;

    /**
    * 受益所有人身份证件有效期限
    */
    @Schema(description = "受益所有人身份证件有效期限")
    private String beneVt;

    /**
    * 受益所有人国别
    */
    @Schema(description = "受益所有人国别")
    private String beneRc;

}
