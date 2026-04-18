package com.zephyr.rule.wrapper;

import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.rule.pojo.entity.AmlPartyAfflt;
import com.zephyr.rule.pojo.vo.AmlPartyAffltVO;

/**
* AmlPartyAfflt包装类,返回视图层所需的字段
*
* @author zephyr
* @since 2025-10-13
*/
public class AmlPartyAffltWrapper extends BaseEntityWrapper<AmlPartyAfflt, AmlPartyAffltVO>  {

    public static AmlPartyAffltWrapper build() {
        return new AmlPartyAffltWrapper();
    }

    @Override
    public AmlPartyAffltVO entityVO(AmlPartyAfflt amlpartyafflt){
        AmlPartyAffltVO amlpartyaffltVO = new AmlPartyAffltVO();
        ZBeanUtils.copyProperties(amlpartyafflt, amlpartyaffltVO);
        return amlpartyaffltVO;
    }
}