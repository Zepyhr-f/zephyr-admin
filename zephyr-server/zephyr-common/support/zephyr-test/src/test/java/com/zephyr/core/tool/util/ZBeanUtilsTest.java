package com.zephyr.core.tool.util;


import com.zephyr.core.tool.annotation.CopyIgnore;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-09-23
 */
public class ZBeanUtilsTest {

    @Data
    static class A {
        int id;
        Map<String,Object> map = new HashMap<>();
        @CopyIgnore
        String name;
    }

    @Data
    static class B {
        int id;
        Map<String,String> map = new HashMap<>();;
        String code;
        String name;
    }


    @Test
    public void copyTest() {
        A a = new A();
        a.id = 1;
        a.map.put("id",new Date());
        a.map.put("code","1");
        a.name = "name";
        B bb = new B();
        B b2 = new B();

        ZBeanUtils.copyProperties(a,bb);
        ZBeanUtils.copyProperties(a,b2,false);

        System.out.println(bb);
        System.out.println(b2);

    }
}