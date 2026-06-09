package com.zephyr.jwt.provider;


import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * JWT Key Provider Factory
 *
 * @author Zephyr
 * @since 2025-09-13
 */
@Component
public class JwtProviderFactory {
    private final Map<String, JwtKeyProvider> providers;

    public JwtProviderFactory(Map<String, JwtKeyProvider> providers) {
        this.providers = providers;
    }

    public JwtKeyProvider getProvider(String alg) {
        JwtKeyProvider provider = providers.get(alg);
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported algorithm: " + alg);
        }
        return provider;
    }
}