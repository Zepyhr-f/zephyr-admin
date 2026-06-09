package com.zephyr.generator.pojo.entity;


import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-09-23
 */
@Getter
public class ZTableInfo extends TableInfo {
    private String wrapperName;
    private String entityVOName;

    public ZTableInfo(@NotNull ConfigBuilder configBuilder, @NotNull String name) {
        super(configBuilder, name);
        initZ(configBuilder, name);
    }

    void initZ(ConfigBuilder configBuilder,String name){
        configBuilder.getTableInfoList().stream()
                .filter(ti -> name.equals(ti.getName()))
                .peek(ti -> super.setComment(ti.getComment()))
                .flatMap(ti -> ti.getFields().stream())
                .forEach(super::addField);
    }

    @Override
    public void processTable() {
        super.processTable();
        String entityName = getEntityName();
        this.wrapperName = entityName + "Wrapper";
        this.entityVOName = entityName + "VO";
    }
}