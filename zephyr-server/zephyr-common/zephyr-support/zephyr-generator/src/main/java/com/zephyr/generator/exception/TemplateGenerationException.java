package com.zephyr.generator.exception;

/**
 * 模板生成异常
 *
 * @author Zephyr
 * @since 2025-09-19
 */
public class TemplateGenerationException extends RuntimeException {
    public TemplateGenerationException(String message) {
        super(message);
    }

    public TemplateGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
