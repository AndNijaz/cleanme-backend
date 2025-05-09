
//Klasa sluzi samo da otkrijem rutu da vidim da li je ispranvo konfigurisan CORS

package com.cleanme.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/test").permitAll()  // ✅ dozvoljeno bez autentifikacije
                        .anyRequest().permitAll()              // ❌ ostalo zahtijeva login
//                        .anyRequest().authenticated()              // ❌ ostalo zahtijeva login
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // ✅ nova sintaksa

        return http.build();
    }

    // ✅ Novi način za definisanje CORS pravila
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
