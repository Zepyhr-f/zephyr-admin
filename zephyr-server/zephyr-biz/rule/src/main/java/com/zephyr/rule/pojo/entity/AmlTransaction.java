package com.zephyr.rule.pojo.entity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
* 交易流水视图类
*
* @author zephyr
* @since 2025-10-13
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "交易流水")
@TableName("AML_TRANSACTION")
public class AmlTransaction extends BaseEntity {

    /**
    * 业务标识
    */
    @Schema(description = "业务标识")
    private String transactionkey;

    /**
    * 核心主键
    */
    @Schema(description = "核心主键")
    private String cbPk;

    /**
    * 流水号
    */
    @Schema(description = "流水号")
    private String txNo;

    /**
    * 传票号
    */
    @Schema(description = "传票号")
    private String voucherNo;

    /**
    * 交易机构
    */
    @Schema(description = "交易机构")
    private String organkey;

    /**
    * 交易日期
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "交易日期")
    private LocalDate txDt;

    /**
    * 交易时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "交易时间")
    private LocalDateTime dtTime;

    /**
    * 我行账户
    */
    @Schema(description = "我行账户")
    private String acctNum;

    /**
    * 我行客户号
    */
    @Schema(description = "我行客户号")
    private String partyId;

    /**
    * 客户类型
    */
    @Schema(description = "客户类型")
    private String partyClassCd;

    /**
    * Aml交易代码
    */
    @Schema(description = "Aml交易代码")
    private String txCd;

    /**
    * 原交易代码
    */
    @Schema(description = "原交易代码")
    private String cbTxCd;

    /**
    * 业务类型
    */
    @Schema(description = "业务类型")
    private String busTypeCd;

    /**
    * 交易类型
    */
    @Schema(description = "交易类型")
    private String txTypeCd;

    /**
    * 借贷标志 D 借 C 贷
    */
    @Schema(description = "借贷标志 D 借 C 贷")
    private String debitCredit;

    /**
    * 收付标志  01收 02 付
    */
    @Schema(description = "收付标志  01收 02 付")
    private String receivePayCd;

    /**
    * 科目号
    */
    @Schema(description = "科目号")
    private String subjectno;

    /**
    * 币种
    */
    @Schema(description = "币种")
    private String currencyCd;

    /**
    * 本外币标志 1本币  2 外币
    */
    @Schema(description = "本外币标志 1本币  2 外币")
    private String currCd;

    /**
    * 原币交易金额
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "原币交易金额")
    private BigDecimal amt;

    /**
    * 折人民币
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "折人民币")
    private BigDecimal cnyAmt;

    /**
    * 折美元
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "折美元")
    private BigDecimal usdAmt;

    /**
    * 余额
    */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "余额")
    private BigDecimal amtVal;

    /**
    * 现/转标志  1 现金  2 转账
    */
    @Schema(description = "现/转标志  1 现金  2 转账")
    private String cashTransFlag;

    /**
    * 汇款标志
    */
    @Schema(description = "汇款标志")
    private String remitTypeCd;

    /**
    * 摘要
    */
    @Schema(description = "摘要")
    private String des;

    /**
    * 是否跨境交易  1是跨境
    */
    @Schema(description = "是否跨境交易  1是跨境")
    private String overareaInd;

    /**
    * 结算方式
    */
    @Schema(description = "结算方式")
    private String settleTypeCd;

    /**
    * 用途
    */
    @Schema(description = "用途")
    private String useDes;

    /**
    * 对方系统Id
    */
    @Schema(description = "对方系统Id")
    private String oppSysId;

    /**
    * 对方是否我行客户
    */
    @Schema(description = "对方是否我行客户")
    private String oppIsparty;

    /**
    * 对方所在地区
    */
    @Schema(description = "对方所在地区")
    private String oppArea;

    /**
    * 对方行号类型
    */
    @Schema(description = "对方行号类型")
    private String oppOrganType;

    /**
    * 对方行号
    */
    @Schema(description = "对方行号")
    private String oppOrgankey;

    /**
    * 对方银行名称
    */
    @Schema(description = "对方银行名称")
    private String oppOrganname;

    /**
    * 对方客户号
    */
    @Schema(description = "对方客户号")
    private String oppPartyId;

    /**
    * 对方名称
    */
    @Schema(description = "对方名称")
    private String oppName;

    /**
    * 对方账号
    */
    @Schema(description = "对方账号")
    private String oppAcctNum;

    /**
    * 对方交易日期
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "对方交易日期")
    private LocalDate oppTxDt;

    /**
    * 对方账户类型
    */
    @Schema(description = "对方账户类型")
    private String oppAcctTypeCd;

    /**
    * 对方证件类型
    */
    @Schema(description = "对方证件类型")
    private String oppCardType;

    /**
    * 对方证件号码
    */
    @Schema(description = "对方证件号码")
    private String oppCardNo;

    /**
    * 对方客户类型
    */
    @Schema(description = "对方客户类型")
    private String oppPartyClassCd;

    /**
    * 抹账标志
    */
    @Schema(description = "抹账标志")
    private String cancelInd;

    /**
    * 发生标志
    */
    @Schema(description = "发生标志")
    private String amtCd;

    /**
    * 批量标志
    */
    @Schema(description = "批量标志")
    private String batchInd;

    /**
    * 柜员
    */
    @Schema(description = "柜员")
    private String teller;

    /**
    * 是否需补录
    */
    @Schema(description = "是否需补录")
    private String reInd;

    /**
    * 处理状态
    */
    @Schema(description = "处理状态")
    private String handleStatusCd;

    /**
    * 当事人中文名称
    */
    @Schema(description = "当事人中文名称")
    private String partyChnName;

    /**
    * 是否已补录
    */
    @Schema(description = "是否已补录")
    private String addtional;

    /**
    * 补录时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "补录时间")
    private LocalDateTime reDt;

    /**
    * 交易方向
    */
    @Schema(description = "交易方向")
    private String txDirect;

    /**
    * 凭证代码
    */
    @Schema(description = "凭证代码")
    private String tokenNo;

    /**
    * 原客户号
    */
    @Schema(description = "原客户号")
    private String hostCustId;

    /**
    * 交易渠道1：柜面2：网银3：电话银行4：ATM5：POS6：手机银行  8: 理财系统   9：其他
    */
    @Schema(description = "交易渠道1：柜面2：网银3：电话银行4：ATM5：POS6：手机银行  8: 理财系统   9：其他")
    private String channel;

    /**
    * 是否计算 0:否1:是
    */
    @Schema(description = "是否计算 0:否1:是")
    private String calInd;

    /**
    * 是否规则 0:大额可疑均不计算;1:仅大额;2:仅可疑;3:大额可疑均计算
    */
    @Schema(description = "是否规则 0:大额可疑均不计算;1:仅大额;2:仅可疑;3:大额可疑均计算")
    private String ruleInd;

    /**
    * 事件类型 01：开户 02：销户 03：提前还贷 04 人工交易
    */
    @Schema(description = "事件类型 01：开户 02：销户 03：提前还贷 04 人工交易")
    private String temp1;

    /**
    * 对方金融机构国家
    */
    @Schema(description = "对方金融机构国家")
    private String oppCountry;

    /**
    * 涉外收支交易代码
    */
    @Schema(description = "涉外收支交易代码")
    private String tsctkey;

    /**
    * 交易去向国别
    */
    @Schema(description = "交易去向国别")
    private String txGoCountry;

    /**
    * 交易去向行政区
    */
    @Schema(description = "交易去向行政区")
    private String txGoArea;

    /**
    * 交易发生地国别
    */
    @Schema(description = "交易发生地国别")
    private String txOccurCountry;

    /**
    * 交易发生地行政区
    */
    @Schema(description = "交易发生地行政区")
    private String txOccurArea;

    /**
    * 代办人姓名
    */
    @Schema(description = "代办人姓名")
    private String agentName;

    /**
    * 代办人身份证件/证明文件类型
    */
    @Schema(description = "代办人身份证件/证明文件类型")
    private String agentCardType;

    /**
    * 代办人身份证件/证明文件号码
    */
    @Schema(description = "代办人身份证件/证明文件号码")
    private String agentCardNo;

    /**
    * 代办人国籍
    */
    @Schema(description = "代办人国籍")
    private String agentCountry;

    /**
    * 金融机构和交易关系
    */
    @Schema(description = "金融机构和交易关系")
    private String orgTransRela;

    /**
    * 现钞标志
    */
    @Schema(description = "现钞标志")
    private String cashInd;

    /**
    * 对方人行客户类型
    */
    @Schema(description = "对方人行客户类型")
    private String oppPbcPartyClassCd;

    /**
    * 复核柜员
    */
    @Schema(description = "复核柜员")
    private String checkTeller;

    /**
    * 上次更新柜员
    */
    @Schema(description = "上次更新柜员")
    private String lastUpdUsr;

    /**
    * 对方是否离岸账户0:否1:是
    */
    @Schema(description = "对方是否离岸账户0:否1:是")
    private String oppOffShoreInd;

    /**
    * 业务代号
    */
    @Schema(description = "业务代号")
    private String bizTypeCd;

    /**
    * 大额验证状态
    */
    @Schema(description = "大额验证状态")
    private String validateInd;

    /**
    * 可疑验证状态
    */
    @Schema(description = "可疑验证状态")
    private String validateInd2;

    /**
    * 商户类型
    */
    @Schema(description = "商户类型")
    private String merchantTypeCd;

    /**
    * 收单商户代码
    */
    @Schema(description = "收单商户代码")
    private String merchantId;

    /**
    * 卡片类型
    */
    @Schema(description = "卡片类型")
    private String acctStyle;

    /**
    * 对方卡片类型
    */
    @Schema(description = "对方卡片类型")
    private String oppAcctStyle;

    /**
    * 所属机构
    */
    @Schema(description = "所属机构")
    private String objorgankey;

    /**
    * 交易IP地址
    */
    @Schema(description = "交易IP地址")
    private String transIp;

    /**
    * 客户银行卡类型 10:借记卡 20:贷记卡 30:准贷记卡 90:其他(new)
    */
    @Schema(description = "客户银行卡类型 10:借记卡 20:贷记卡 30:准贷记卡 90:其他(new)")
    private String bankCardType;

    /**
    * 客户银行卡其他类型
    */
    @Schema(description = "客户银行卡其他类型")
    private String otherBankCardType;

    /**
    * 客户银行卡号码
    */
    @Schema(description = "客户银行卡号码")
    private String bankCardNo;

    /**
    * 收付款方匹配号类型01,02,03,04,11
    */
    @Schema(description = "收付款方匹配号类型01,02,03,04,11")
    private String receivePayType;

    /**
    * 收付款方匹配号
    */
    @Schema(description = "收付款方匹配号")
    private String receivePayNum;

    /**
    * 非柜台交易方式11：网上交易、12：通过POS机交易、13：通过电话交易、14：通过热键机交易、15：通过ATM、16：通过传真交易、17：营业场所交易、99：其他
    */
    @Schema(description = "非柜台交易方式11：网上交易、12：通过POS机交易、13：通过电话交易、14：通过热键机交易、15：通过ATM、16：通过传真交易、17：营业场所交易、99：其他")
    private String channel1;

    /**
    * 其他非柜台交易方式
    */
    @Schema(description = "其他非柜台交易方式")
    private String otherChannel;

    /**
    * 非柜台交易方式的设备代码
    */
    @Schema(description = "非柜台交易方式的设备代码")
    private String channelNum;

    /**
    * 银行与支付机构之间的业务交易编码
    */
    @Schema(description = "银行与支付机构之间的业务交易编码")
    private String bankReceivePayNum;

    /**
    * 交易信息备注1
    */
    @Schema(description = "交易信息备注1")
    private String mark1;

    /**
    * 交易信息备注2
    */
    @Schema(description = "交易信息备注2")
    private String mark2;

    /**
    * 其他代办人证件类型
    */
    @Schema(description = "其他代办人证件类型")
    private String agentOttherCardType;

    /**
    * 对手其他证件类型
    */
    @Schema(description = "对手其他证件类型")
    private String oppOtherCardType;

    /**
    * 代办理人客户号
    */
    @Schema(description = "代办理人客户号")
    private String agentPartyId;

    /**
    * 网联清算平台ljf  值是1时交易对手网点名称、网点代码均不校验
    */
    @Schema(description = "网联清算平台ljf  值是1时交易对手网点名称、网点代码均不校验")
    private String oppFlag;

    /**
    * 跨行标记  0 同行  1 跨行
    */
    @Schema(description = "跨行标记  0 同行  1 跨行")
    private String overbankInd;

    /**
    * 单笔现金个人30W 单位50W资金用途需要报送用途码表  如果码表为空此字段 0  待办任务处理完成后更新为 1
    */
    @Schema(description = "单笔现金个人30W 单位50W资金用途需要报送用途码表  如果码表为空此字段 0  待办任务处理完成后更新为 1")
    private String useDesFlag;

    /**
    * 客户银行账户开户行
    */
    @Schema(description = "客户银行账户开户行")
    private String bankName;

}
