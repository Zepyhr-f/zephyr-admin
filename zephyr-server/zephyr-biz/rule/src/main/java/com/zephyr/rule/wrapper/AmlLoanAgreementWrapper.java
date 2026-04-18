package com.zephyr.rule.wrapper;

import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.rule.pojo.entity.AmlLoanAgreement;
import com.zephyr.rule.pojo.vo.AmlLoanAgreementVO;

/**
* AmlLoanAgreement包装类,返回视图层所需的字段
*
* @author zephyr
* @since 2025-10-13
*/
public class AmlLoanAgreementWrapper extends BaseEntityWrapper<AmlLoanAgreement, AmlLoanAgreementVO>  {

    public static AmlLoanAgreementWrapper build() {
        return new AmlLoanAgreementWrapper();
    }

    @Override
    public AmlLoanAgreementVO entityVO(AmlLoanAgreement amlloanagreement){
        AmlLoanAgreementVO amlloanagreementVO = new AmlLoanAgreementVO();
        ZBeanUtils.copyProperties(amlloanagreement, amlloanagreementVO);
        return amlloanagreementVO;
    }
}