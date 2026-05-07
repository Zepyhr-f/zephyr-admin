package ${packageName}.pojo.entity;


<#list baseImports as imp>
import ${imp};
</#list>

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableName;
<#list jarImports as imp>
import ${imp};
</#list>

import com.zephyr.mp.base.BaseEntity;


/**
* ${comment!}视图类
*
* @author ${author!}
* @since ${date!}
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "${comment!}")
@TableName("${tableName}")
public class ${className} extends BaseEntity {

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
