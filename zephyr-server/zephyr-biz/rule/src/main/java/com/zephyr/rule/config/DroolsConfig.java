package com.zephyr.rule.config;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-10-13
 */
@Configuration
public class DroolsConfig {

    @Bean
    public KieContainer kieContainer() {
        return KieServices.Factory.get().getKieClasspathContainer();
    }

    @Bean
    public KieSession kieSession(KieContainer kieContainer) {
        return kieContainer.newKieSession("zephyr-rule");
    }
}