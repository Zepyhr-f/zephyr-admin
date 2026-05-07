package ${packageName}.pojo.vo;


import java.io.Serial;
import java.io.Serializable;
<#list baseImports as imp>
import ${imp};
</#list>

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
<#list jarImports as imp>
import ${imp};
</#list>


/**
* ${comment!}实体类
*
* @author ${author!}
* @since ${date!}
*/
@Data
@Schema(description = "${comment!}")
public class ${className}VO implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键id")
    Long id;

<#list fields as field>
    /**
    * ${field.comment!}
    */
    <#if field.serializeToString>
    @JsonSerialize(using = ToStringSerializer.class)
    </#if>
    <#if field.dateFormat??>
    @DateTimeFormat(pattern = "${field.dateFormat}")
    </#if>
    <#if field.dateFormat??>
    @JsonFormat(pattern = "${field.dateFormat}")
    </#if>
    @Schema(description = "${field.comment!}")
    private ${field.type} ${field.name};

</#list>
}
