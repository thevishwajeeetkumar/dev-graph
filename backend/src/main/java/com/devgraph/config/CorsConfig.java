package com.devgraph.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Locks CORS to an explicit, operator-configured origin list
 * ({@code app.cors.allowed-origins}, comma-separated). There is
 * intentionally no wildcard fallback: an empty/blank value disables
 * cross-origin access entirely rather than defaulting to "*".
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

    @Value("${app.cors.allowed-origins:}")
    private String allowedOriginsProperty;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> allowedOrigins = Arrays.stream(allowedOriginsProperty.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();

        if (allowedOrigins.isEmpty()) {
            log.warn("app.cors.allowed-origins is not set - cross-origin requests will be rejected");
            return;
        }

        log.info("CORS enabled for origins: {}", allowedOrigins);
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins.toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
