package com.zephyr.rule.pojo.vo;


import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;


/**
* 当事人信息实体类
*
* @author zephyr
* @since 2025-10-13
*/
@Data
@Schema(description = "当事人信息")
public class AmlPartyVO implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键id")
    Long id;

    /**
    * 当事人编号
    */
    @Schema(description = "当事人编号")
    private String partyId;

    /**
    * 原客户号
    */
    @Schema(description = "原客户号")
    private String hostCustId;

    /**
    * 当事人类型 C:对公当事人 I:个人当事人
    */
    @Schema(description = "当事人类型 C:对公当事人 I:个人当事人")
    private String partyClassCd;

    /**
    * 是否新增客户:0:否1:是
    */
    @Schema(description = "是否新增客户:0:否1:是")
    private String newInd;

    /**
    * AML类型1:个人：01居民 02非居民公司：51:各级党的机关、国家权力机关、行政机关、司法机关、军事机关、人民政协机关52:人民解放军、武警部队53:保险机构54:证券经营机构
    */
    @Schema(description = "AML类型1:个人：01居民 02非居民公司：51:各级党的机关、国家权力机关、行政机关、司法机关、军事机关、人民政协机关52:人民解放军、武警部队53:保险机构54:证券经营机构")
    private String aml1TypeCd;

    /**
    * AML类型2 保留
    */
    @Schema(description = "AML类型2 保留")
    private String aml2TypeCd;

    /**
    * 当事人状态:0：正常1：销户2：未开户4：删除X：未知
    */
    @Schema(description = "当事人状态:0：正常1：销户2：未开户4：删除X：未知")
    private String partyStatusCd;

    /**
    * 当事人中文名称
    */
    @Schema(description = "当事人中文名称")
    private String partyChnName;

    /**
    * 当事人英文名称
    */
    @Schema(description = "当事人英文名称")
    private String partyEngName;

    /**
    * 证件类型 01:居民身份证、临时居民身份证或户口簿02：军人身份证件或警察身份证件03：港澳居民往来内地通行证、台湾居民往来内地通行证或者其他有效旅行证件04：护照05：其他51:机构代码证
    */
    @Schema(description = "证件类型 01:居民身份证、临时居民身份证或户口簿02：军人身份证件或警察身份证件03：港澳居民往来内地通行证、台湾居民往来内地通行证或者其他有效旅行证件04：护照05：其他51:机构代码证")
    private String cardType;

    /**
    * 证件号码
    */
    @Schema(description = "证件号码")
    private String cardNo;

    /**
    * 生日
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "生日")
    private LocalDate birthDt;

    /**
    * 所在地
    */
    @Schema(description = "所在地")
    private String countryResidence;

    /**
    * 所属国家
    */
    @Schema(description = "所属国家")
    private String countryCd;

    /**
    * 地址1
    */
    @Schema(description = "地址1")
    private String addr1;

    /**
    * 地址2
    */
    @Schema(description = "地址2")
    private String addr2;

    /**
    * 固定电话
    */
    @Schema(description = "固定电话")
    private String telNo;

    /**
    * 移动电话
    */
    @Schema(description = "移动电话")
    private String cellNo;

    /**
    * 网址
    */
    @Schema(description = "网址")
    private String netAddress;

    /**
    * 电子邮件
    */
    @Schema(description = "电子邮件")
    private String emailAddr;

    /**
    * 建立时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "建立时间")
    private LocalDateTime createDt;

    /**
    * 上次更新时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "上次更新时间")
    private LocalDateTime lastUpdDt;

    /**
    * 上次更新用户
    */
    @Schema(description = "上次更新用户")
    private String lastUpdUser;

    /**
    * 所属机构
    */
    @Schema(description = "所属机构")
    private String organkey;

    /**
    * 邮政编码
    */
    @Schema(description = "邮政编码")
    private String postalcode;

    /**
    * 保留1
    */
    @Schema(description = "保留1")
    private String temp1;

    /**
    * 保留2
    */
    @Schema(description = "保留2")
    private String temp2;

    /**
    * 是否代理开户
    */
    @Schema(description = "是否代理开户")
    private String isAgent;

    /**
    * 国内风险
    */
    @Schema(description = "国内风险")
    private String guoneifengxian;

    /**
    * 国际风险
    */
    @Schema(description = "国际风险")
    private String guojifengxian;

    /**
    * 反洗钱风险
    */
    @Schema(description = "反洗钱风险")
    private String fanxiqianjiank;

    /**
    * 所属机构
    */
    @Schema(description = "所属机构")
    private String objorgankey;

    /**
    * 销户后重新开户时间
    */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "销户后重新开户时间")
    private LocalDateTime nextCreateDt;

    /**
    * 是否上市企业
    */
    @Schema(description = "是否上市企业")
    private String lstFlag;

    /**
    * 境内境外
    */
    @Schema(description = "境内境外")
    private String inout;

    /**
    * 企业收入级别分类
    */
    @Schema(description = "企业收入级别分类")
    private String srTp;

    /**
    * 是否自贸区客户
    */
    @Schema(description = "是否自贸区客户")
    private String freeTradeArea;

    /**
    * 负责人姓名
    */
    @Schema(description = "负责人姓名")
    private String pcnm;

    /**
    * 负责人身份证件/证明文件类型
    */
    @Schema(description = "负责人身份证件/证明文件类型")
    private String pitp;

    /**
    * 负责人身份证件/证明文件号码
    */
    @Schema(description = "负责人身份证件/证明文件号码")
    private String picd;

    /**
    * 负责人身份证件/证明文件有效期限
    */
    @Schema(description = "负责人身份证件/证明文件有效期限")
    private String pivt;

    /**
    * 客户信息虚假标识
    */
    @Schema(description = "客户信息虚假标识")
    private String cifi;

    /**
    * 客户涉及风险提示和媒体报道
    */
    @Schema(description = "客户涉及风险提示和媒体报道")
    private String crmc;

    /**
    * 法定代表人涉及风险提示和媒体报道
    */
    @Schema(description = "法定代表人涉及风险提示和媒体报道")
    private String rrmc;

    /**
    * 股东涉及风险提示和媒体报道
    */
    @Schema(description = "股东涉及风险提示和媒体报道")
    private String srmc;

    /**
    * 担保人涉及风险提示和媒体报道
    */
    @Schema(description = "担保人涉及风险提示和媒体报道")
    private String grmc;

    /**
    * 业务关系建立渠道
    */
    @Schema(description = "业务关系建立渠道")
    private String brbc;

    /**
    * 主营业务地区
    */
    @Schema(description = "主营业务地区")
    private String mbsa;

    /**
    * 法定代表人证件地址
    */
    @Schema(description = "法定代表人证件地址")
    private String reprCertAddr;

    /**
    * 法定代表人居住地
    */
    @Schema(description = "法定代表人居住地")
    private String reprLiveAddr;

    /**
    * 法定代表人联系地
    */
    @Schema(description = "法定代表人联系地")
    private String reprContAddr;

    /**
    * 负责人证件地址
    */
    @Schema(description = "负责人证件地址")
    private String rechCertAddr;

    /**
    * 负责人居住地
    */
    @Schema(description = "负责人居住地")
    private String rechLiveAddr;

    /**
    * 负责人联系地
    */
    @Schema(description = "负责人联系地")
    private String rechContAddr;

    /**
    * 业务关系建立日期
    */
    @Schema(description = "业务关系建立日期")
    private String brbt;

    /**
    * 业务关系结束日期
    */
    @Schema(description = "业务关系结束日期")
    private String bret;

    /**
    * 是否同业客户
    */
    @Schema(description = "是否同业客户")
    private String isSamecust;

    /**
    * 境内企业控股类型
    */
    @Schema(description = "境内企业控股类型")
    private String hldTp;

    /**
    * 是否是中资
    */
    @Schema(description = "是否是中资")
    private String cnHldrFlg;

    /**
    * 数据来源
    */
    @Schema(description = "数据来源")
    private String source;

}
