package com.zephyr.rule.wrapper;

import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.rule.pojo.entity.AmlTransaction;
import com.zephyr.rule.pojo.vo.AmlTransactionVO;

/**
* AmlTransaction包装类,返回视图层所需的字段
*
* @author zephyr
* @since 2025-10-13
*/
public class AmlTransactionWrapper extends BaseEntityWrapper<AmlTransaction, AmlTransactionVO>  {

    public static AmlTransactionWrapper build() {
        return new AmlTransactionWrapper();
    }

    @Override
    public AmlTransactionVO entityVO(AmlTransaction amltransaction){
        AmlTransactionVO amltransactionVO = new AmlTransactionVO();
        ZBeanUtils.copyProperties(amltransaction, amltransactionVO);
        return amltransactionVO;
    }
}