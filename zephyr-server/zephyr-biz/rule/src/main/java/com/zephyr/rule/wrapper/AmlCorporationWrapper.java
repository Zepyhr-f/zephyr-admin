package com.zephyr.rule.wrapper;

import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.rule.pojo.entity.AmlCorporation;
import com.zephyr.rule.pojo.vo.AmlCorporationVO;

/**
* AmlCorporation包装类,返回视图层所需的字段
*
* @author zephyr
* @since 2025-10-13
*/
public class AmlCorporationWrapper extends BaseEntityWrapper<AmlCorporation, AmlCorporationVO>  {

    public static AmlCorporationWrapper build() {
        return new AmlCorporationWrapper();
    }

    @Override
    public AmlCorporationVO entityVO(AmlCorporation amlcorporation){
        AmlCorporationVO amlcorporationVO = new AmlCorporationVO();
        ZBeanUtils.copyProperties(amlcorporation, amlcorporationVO);
        return amlcorporationVO;
    }
}