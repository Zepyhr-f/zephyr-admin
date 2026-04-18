package com.zephyr.rule.pojo.vo;


import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
* 租赁协议信息实体类
*
* @author zephyr
* @since 2025-10-13
*/
@Data
@Schema(description = "租赁协议信息")
public class AmlLoanAgreementVO implements Serializable{

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
    * 主合同编号
    */
    @Schema(description = "主合同编号")
    private String contNo;

    /**
    * 子合同编号
    */
    @Schema(description = "子合同编号")
    private String contSubNo;

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
    * 签约日期
    */
    @Schema(description = "签约日期")
    private String appoDate;

    /**
    * 到期日期
    */
    @Schema(description = "到期日期")
    private String endDate;

    /**
    * 放款金额
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "放款金额")
    private BigDecimal loanAmt;

    /**
    * 业务/产品类型
    */
    @Schema(description = "业务/产品类型")
    private String prodType;

    /**
    * 供货商证件类型
    */
    @Schema(description = "供货商证件类型")
    private String suppType;

    /**
    * 供货商证件号
    */
    @Schema(description = "供货商证件号")
    private String suppNo;

    /**
    * 供货商名称
    */
    @Schema(description = "供货商名称")
    private String suppName;

    /**
    * 租赁物价值
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "租赁物价值")
    private BigDecimal leaseValue;

    /**
    * 状态
    */
    @Schema(description = "状态")
    private String state;

    /**
    * 放款日期
    */
    @Schema(description = "放款日期")
    private String loanDate;

    /**
    * 还款日
    */
    @Schema(description = "还款日")
    private String repayDate;

    /**
    * 还款周期
    */
    @Schema(description = "还款周期")
    private String repayCycle;

    /**
    * 总期数
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "总期数")
    private BigDecimal totalPeriod;

    /**
    * 放款账号
    */
    @Schema(description = "放款账号")
    private String loanNo;

    /**
    * 放款账户名称
    */
    @Schema(description = "放款账户名称")
    private String loanAccountName;

    /**
    * 放款账户证件类型
    */
    @Schema(description = "放款账户证件类型")
    private String loanAccountCredType;

    /**
    * 放款账户证件号码
    */
    @Schema(description = "放款账户证件号码")
    private String loanAccountCredNo;

    /**
    * 放款账户类型
    */
    @Schema(description = "放款账户类型")
    private String loanAccountType;

    /**
    * 放款账户所属金融机构代码
    */
    @Schema(description = "放款账户所属金融机构代码")
    private String loanAccountFic;

    /**
    * 放款账户所属金融机构名称
    */
    @Schema(description = "放款账户所属金融机构名称")
    private String loanAccountFin;

    /**
    * 租赁物与公司经营业务是否相符
    */
    @Schema(description = "租赁物与公司经营业务是否相符")
    private String isConform;

    /**
    * 还款账号
    */
    @Schema(description = "还款账号")
    private String repayNo;

    /**
    * 还款账户名称
    */
    @Schema(description = "还款账户名称")
    private String repayAccountName;

    /**
    * 还款账户证件类型
    */
    @Schema(description = "还款账户证件类型")
    private String repayAccountCredType;

    /**
    * 还款账户证件号码
    */
    @Schema(description = "还款账户证件号码")
    private String repayAccountCredNo;

    /**
    * 还款账户类型
    */
    @Schema(description = "还款账户类型")
    private String repayAccountType;

    /**
    * 还款账户所属金融机构代码
    */
    @Schema(description = "还款账户所属金融机构代码")
    private String repayAccountFic;

    /**
    * 还款账户所属金融机构名称
    */
    @Schema(description = "还款账户所属金融机构名称")
    private String repayAccountFin;

    /**
    * 还款账户境外标识
    */
    @Schema(description = "还款账户境外标识")
    private String isOut;

    /**
    * 代理签约标识
    */
    @Schema(description = "代理签约标识")
    private String isSigncont;

    /**
    * 代理人证件类型
    */
    @Schema(description = "代理人证件类型")
    private String agentCredType;

    /**
    * 代理人证件号码
    */
    @Schema(description = "代理人证件号码")
    private String agentCredNo;

    /**
    * 代理人名称
    */
    @Schema(description = "代理人名称")
    private String agentName;

    /**
    * 代理人联系方式
    */
    @Schema(description = "代理人联系方式")
    private String agentCi;

    /**
    * 客户经理编号
    */
    @Schema(description = "客户经理编号")
    private String accountManagerNo;

    /**
    * 客户经理名称
    */
    @Schema(description = "客户经理名称")
    private String accountManagerName;

    /**
    * 币种
    */
    @Schema(description = "币种")
    private String currency;

    /**
    * 担保类型：A,B,C,D,。含义参考文档
    */
    @Schema(description = "担保类型：A,B,C,D,。含义参考文档")
    private String guarType;

}
