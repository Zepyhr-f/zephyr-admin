package ${packageName}.wrapper;

import com.zephyr.mp.base.BaseEntityWrapper;
import ${packageName}.convert.${className}Convert;
import ${packageName}.pojo.entity.${className};
import ${packageName}.pojo.vo.${className}VO;

/**
* ${className}包装类,返回视图层所需的字段
*
* @author ${author!}
* @since ${date!}
*/
public class ${className}Wrapper extends BaseEntityWrapper<${className}, ${className}VO>  {

    private final ${className}Convert ${className?lower_case}Convert = ${className}Convert.INSTANCE;

    public static ${className}Wrapper build() {
        return new ${className}Wrapper();
    }

    @Override
    public ${className}VO entityVO(${className} ${className?lower_case}){
        return ${className?lower_case}Convert.toVo(${className?lower_case});
    }
}