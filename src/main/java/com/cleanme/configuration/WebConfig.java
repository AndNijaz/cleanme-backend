
package com.cleanme.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.url}") // CORS whitelist – koristi URL iz application.properties
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendUrl)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @PostConstruct
    public void printFrontendUrl() {
        System.out.println("🌍 CORS ALLOWED ORIGIN: " + frontendUrl);
    }
}

//Setup komunikacije fronta i beka - NE BRISATI, eventualno promijeniti




