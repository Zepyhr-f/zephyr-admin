package com.zephyr.rule.pojo.entity;


import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import com.zephyr.mp.base.BaseEntity;


/**
* 公司信息视图类
*
* @author zephyr
* @since 2025-10-13
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "公司信息")
@TableName("AML_CORPORATION")
public class AmlCorporation extends BaseEntity {

    /**
    * 当事人编号
    */
    @Schema(description = "当事人编号")
    private String partyId;

    /**
    * 企业类型
    */
    @Schema(description = "企业类型")
    private String companyTypeCd;

    /**
    * 行业
    */
    @Schema(description = "行业")
    private String industrykey;

    /**
    * 发照日期
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "发照日期")
    private LocalDate issueLicenseDt;

    /**
    * 营业执照号
    */
    @Schema(description = "营业执照号")
    private String businessLicence;

    /**
    * 执照年检日期
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "执照年检日期")
    private LocalDate licenseYearCheckDt;

    /**
    * 主营范围
    */
    @Schema(description = "主营范围")
    private String mainManageScope;

    /**
    * 注册资本
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "注册资本")
    private BigDecimal enrolFundAmt;

    /**
    * 注册资本币别代码
    */
    @Schema(description = "注册资本币别代码")
    private String enrolFundCurrencyCd;

    /**
    * 外商投资批准证书号
    */
    @Schema(description = "外商投资批准证书号")
    private String forgInvestCertNum;

    /**
    * 基本帐户
    */
    @Schema(description = "基本帐户")
    private String baseAcctNum;

    /**
    * 基本户开户行
    */
    @Schema(description = "基本户开户行")
    private String baseBankId;

    /**
    * 基本户开户行支行
    */
    @Schema(description = "基本户开户行支行")
    private String baseBankOrg;

    /**
    * 企业规模
    */
    @Schema(description = "企业规模")
    private String companyScope;

    /**
    * 所有制形式
    */
    @Schema(description = "所有制形式")
    private String havingSysForm;

    /**
    * 特殊行业许可证号
    */
    @Schema(description = "特殊行业许可证号")
    private String specIndusAllowCertId;

    /**
    * 贷款卡号
    */
    @Schema(description = "贷款卡号")
    private String loanCardNum;

    /**
    * 是否集团客户
    */
    @Schema(description = "是否集团客户")
    private String groupCustInd;

    /**
    * 法定代表人
    */
    @Schema(description = "法定代表人")
    private String legalObj;

    /**
    * 法定代表人证件类型
    */
    @Schema(description = "法定代表人证件类型")
    private String legalCardType;

    /**
    * 法定代表人证件号码
    */
    @Schema(description = "法定代表人证件号码")
    private String legalCardNo;

    /**
    * 启用标志0-禁用1-启用
    */
    @Schema(description = "启用标志0-禁用1-启用")
    private String flag;

    /**
    * 导入系统时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "导入系统时间")
    private LocalDate incomeDt;

    /**
    * 其他补充信息
    */
    @Schema(description = "其他补充信息")
    private String otherInfo;

    /**
    * 客户经理名
    */
    @Schema(description = "客户经理名")
    private String managerName;

    /**
    * 客户经理号
    */
    @Schema(description = "客户经理号")
    private String managerNo;

    /**
    * 地税登记证号
    */
    @Schema(description = "地税登记证号")
    private String localAffairNo;

    /**
    * 账户种类
    */
    @Schema(description = "账户种类")
    private String catpkey;

    /**
    * 国税登记证号
    */
    @Schema(description = "国税登记证号")
    private String nationAffairNo;

    /**
    * 组织机构代码
    */
    @Schema(description = "组织机构代码")
    private String organCode;

    /**
    * 法人居住地
    */
    @Schema(description = "法人居住地")
    private String legalAddr;

    /**
    * 法人联系方式
    */
    @Schema(description = "法人联系方式")
    private String legalTel;

    /**
    * 营业执照到期日
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "营业执照到期日")
    private LocalDate licenceEndDt;

    /**
    * 我行账户
    */
    @Schema(description = "我行账户")
    private String acctNum;

    /**
    * 上次更新用户
    */
    @Schema(description = "上次更新用户")
    private String lastUpdUser;

    /**
    * 企业联系人
    */
    @Schema(description = "企业联系人")
    private String contact;

    /**
    * 企业联系人电话
    */
    @Schema(description = "企业联系人电话")
    private String contactTelNo;

    /**
    * 法人证件到期日
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "法人证件到期日")
    private LocalDate legalCardNoEndDt;

    /**
    * 地税登记证到期日
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "地税登记证到期日")
    private LocalDate localAffairEndDt;

    /**
    * 国税登记证到期日
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "国税登记证到期日")
    private LocalDate nationAffairEndDt;

    /**
    * 组织机构代码到期日
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "组织机构代码到期日")
    private LocalDate organCodeEndDt;

    /**
    * 上次更新时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "上次更新时间")
    private LocalDate lastUpdDt;

    /**
    * 存款人类别
    */
    @Schema(description = "存款人类别")
    private String depositerTy;

    /**
    * 基本存款账户开户许可证号
    */
    @Schema(description = "基本存款账户开户许可证号")
    private String permitNo;

    /**
    * 批件（营业执照）的名称
    */
    @Schema(description = "批件（营业执照）的名称")
    private String licenseName;

    /**
    * 注册日期
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "注册日期")
    private LocalDate registerDt;

    /**
    * 注册地
    */
    @Schema(description = "注册地")
    private String registerAddr;

    /**
    * 单位负责人
    */
    @Schema(description = "单位负责人")
    private String subjectOfficer;

    /**
    * 开户单位上级法人
    */
    @Schema(description = "开户单位上级法人")
    private String superiorLegal;

    /**
    * 控股股东
    */
    @Schema(description = "控股股东")
    private String stockHolder;

    /**
    * 实际收益人
    */
    @Schema(description = "实际收益人")
    private String acutProf;

    /**
    * 实际控制人身份证件有效期限
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "实际控制人身份证件有效期限")
    private LocalDate stockHolderCardNoEndDt;

    /**
    * 实际控制人身份证件号码
    */
    @Schema(description = "实际控制人身份证件号码")
    private String stockHolderCardNo;

    /**
    * 实际控制人身份证件种类
    */
    @Schema(description = "实际控制人身份证件种类")
    private String stockHolderCardType;

    /**
    * 机构信用代码
    */
    @Schema(description = "机构信用代码")
    private String orgCreditNo;

    /**
    * 信用代码到期日
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "信用代码到期日")
    private LocalDate orgCreditVt;

    /**
    * 所属机构
    */
    @Schema(description = "所属机构")
    private String objorgankey;

    /**
    * 法人客户号
    */
    @Schema(description = "法人客户号")
    private String legalPartyId;

    /**
    * 主营业务
    */
    @Schema(description = "主营业务")
    private String mainManageBusiness;

    /**
    * 实际控制国籍
    */
    @Schema(description = "实际控制国籍")
    private String stockHolderCountry;

    /**
    * 法定代表人其他证件类型
    */
    @Schema(description = "法定代表人其他证件类型")
    private String legalOtherCardType;

    /**
    * 实际控制人其他证件类型
    */
    @Schema(description = "实际控制人其他证件类型")
    private String stockHolderOtherCardType;

    /**
    * 授权代理人
    */
    @Schema(description = "授权代理人")
    private String authorAgent;

    /**
    * 授权代理人证件到期日
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "授权代理人证件到期日")
    private LocalDate authorAgentCardNoEndDt;

    /**
    * 授权代理人证件号码
    */
    @Schema(description = "授权代理人证件号码")
    private String authorAgentCardNo;

    /**
    * 授权代理人证件种类
    */
    @Schema(description = "授权代理人证件种类")
    private String authorAgentCardType;

    /**
    * 事务编号
    */
    @Schema(description = "事务编号")
    private String affairNo;

    /**
    * 事务结束日期
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "事务结束日期")
    private LocalDate affairEndDt;

}
