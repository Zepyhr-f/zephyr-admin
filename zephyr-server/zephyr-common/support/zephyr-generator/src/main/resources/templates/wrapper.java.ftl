package ${packageName}.wrapper;

import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.mp.base.BaseEntityWrapper;
import ${packageName}.pojo.entity.${className};
import ${packageName}.pojo.vo.${className}VO;

/**
* ${className}包装类,返回视图层所需的字段
*
* @author ${author!}
* @since ${date!}
*/
public class ${className}Wrapper extends BaseEntityWrapper<${className}, ${className}VO>  {

    public static ${className}Wrapper build() {
        return new ${className}Wrapper();
    }

    @Override
    public ${className}VO entityVO(${className} ${className?lower_case}){
        ${className}VO ${className?lower_case}VO = new ${className}VO();
        ZBeanUtils.copyProperties(${className?lower_case}, ${className?lower_case}VO);
        return ${className?lower_case}VO;
    }
}