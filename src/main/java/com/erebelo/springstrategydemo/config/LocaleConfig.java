package com.erebelo.springstrategydemo.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

/**
 * Configures a fixed English locale for the application.
 * <p>
 * This ensures that framework-provided messages, including Jakarta Bean
 * Validation messages, are consistently resolved in English regardless of the
 * client's locale or browser language settings.
 */
@Configuration
public class LocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.US);
    }
}
