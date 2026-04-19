package com.zephyr.ops.util;

import com.zephyr.core.tool.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class JobInvokeUtil {

    public static void invokeMethod(String invokeTarget) throws Exception {
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        Object[] params = getMethodParams(invokeTarget);

        Object bean = SpringUtil.getBean(beanName);
        invoke(bean, methodName, params);
    }

    private static String getBeanName(String invokeTarget) {
        return invokeTarget.substring(0, invokeTarget.indexOf("("));
    }

    private static String getMethodName(String invokeTarget) {
        String tmp = invokeTarget.substring(0, invokeTarget.indexOf("("));
        return tmp.substring(tmp.lastIndexOf(".") + 1);
    }

    private static Object[] getMethodParams(String invokeTarget) {
        // Simple placeholder for params parsing
        return new Object[]{};
    }

    private static void invoke(Object bean, String methodName, Object[] params) 
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = bean.getClass().getDeclaredMethod(methodName);
        method.invoke(bean, params);
    }
}
