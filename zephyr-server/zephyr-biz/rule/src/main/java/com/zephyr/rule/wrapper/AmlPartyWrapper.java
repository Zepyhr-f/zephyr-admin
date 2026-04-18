package com.zephyr.rule.wrapper;

import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.rule.pojo.entity.AmlParty;
import com.zephyr.rule.pojo.vo.AmlPartyVO;

/**
* AmlParty包装类,返回视图层所需的字段
*
* @author zephyr
* @since 2025-10-13
*/
public class AmlPartyWrapper extends BaseEntityWrapper<AmlParty, AmlPartyVO>  {

    public static AmlPartyWrapper build() {
        return new AmlPartyWrapper();
    }

    @Override
    public AmlPartyVO entityVO(AmlParty amlparty){
        AmlPartyVO amlpartyVO = new AmlPartyVO();
        ZBeanUtils.copyProperties(amlparty, amlpartyVO);
        return amlpartyVO;
    }
}