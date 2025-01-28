package com.rescuewheels.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // disable authentication for all routes for testing purposes
        http
                .authorizeHttpRequests(configurer ->
                        configurer.anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);
        
        return http.build();
    }
}
