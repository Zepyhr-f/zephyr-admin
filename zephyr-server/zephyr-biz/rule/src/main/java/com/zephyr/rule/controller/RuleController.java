package com.zephyr.rule.controller;


import com.zephyr.rule.model.Person;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-10-13
 */
@RestController
@RequestMapping("/rule")
@AllArgsConstructor
@Tag(name = "规则", description = "规则")
public class RuleController {

    private final KieSession kieSession;

    @PostMapping("/person")
    public Person person(@RequestBody Person person) {
        kieSession.insert(person);
        kieSession.fireAllRules();
        return person;
    }
}