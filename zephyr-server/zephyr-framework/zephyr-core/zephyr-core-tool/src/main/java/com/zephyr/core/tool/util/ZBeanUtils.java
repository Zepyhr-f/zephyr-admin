package com.zephyr.core.tool.util;


import com.zephyr.core.tool.annotation.CopyIgnore;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 拷贝类
 *
 * @author Zephyr
 * @since 2025-09-23
 */
public class ZBeanUtils extends BeanUtils {

    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, true);
    }

    public static void copyProperties(Object source, Object target, boolean ignoreNull) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        // 获取源对象和目标对象的属性描述符
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());
        PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(source.getClass());

        // 构建源属性映射，缓存 Field 和 PropertyDescriptor
        Map<String, PropertyDescriptor> sourceMap = Arrays.stream(sourcePds)
                .collect(Collectors.toMap(PropertyDescriptor::getName, p -> p));

        Arrays.stream(targetPds)
                .filter(targetPd -> targetPd.getWriteMethod() != null) // 只处理可写属性
                .forEach(targetPd -> {
                    try {
                        PropertyDescriptor sourcePd = sourceMap.get(targetPd.getName());
                        if (sourcePd == null || sourcePd.getReadMethod() == null) return;

                        // 查找字段，支持父类
                        if(ignoreNull){
                            Field field = ReflectionUtils.findField(source.getClass(), targetPd.getName());
                            if (field != null && field.isAnnotationPresent(CopyIgnore.class)) return;
                        }

                        Method readMethod = sourcePd.getReadMethod();
                        Method writeMethod = targetPd.getWriteMethod();

                        ReflectionUtils.makeAccessible(readMethod);
                        Object value = readMethod.invoke(source);

                        if (value != null) { // 只拷贝非空属性
                            ReflectionUtils.makeAccessible(writeMethod);
                            writeMethod.invoke(target, value);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(
                                "Failed to copy property '" + targetPd.getName() + "' from " +
                                        source.getClass().getName() + " to " + target.getClass().getName(), e);
                    }
                });
    }
}