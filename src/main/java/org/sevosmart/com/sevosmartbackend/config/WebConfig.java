package org.sevosmart.com.sevosmartbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class WebConfig {

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Allow cookies and authentication data with CORS
        config.addAllowedHeader("*"); // Allow all headers
        config.addAllowedMethod("*"); // Allow all HTTP methods

        // Using allowedOriginPatterns for more flexible origin matching
        config.addAllowedOriginPattern("http://localhost:5173"); // Specific local origin
        config.addAllowedOriginPattern("https://www.sevosmartech.com"); // Specific production origin
        config.addAllowedOriginPattern("https://*.sevosmartech.com"); // Wildcard subdomains

        config.addExposedHeader("ETag"); // Ensure the client can read the ETag header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply this configuration to all paths
        return new CorsFilter(source);
    }
}
