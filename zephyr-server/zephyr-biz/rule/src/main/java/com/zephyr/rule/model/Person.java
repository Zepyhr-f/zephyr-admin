package com.zephyr.rule.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-10-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String name;
    private int age;
    private boolean adult;
}