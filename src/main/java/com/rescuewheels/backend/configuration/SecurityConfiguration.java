package com.rescuewheels.backend.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfiguration(
            AuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/vehicles").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/vehicles/**")
                                    .hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/users/technician").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/emergency-requests")
                                    .hasAnyRole("ADMIN", "USER")
                                .requestMatchers(HttpMethod.GET, "/emergency-requests").hasRole("ADMIN")
                                .requestMatchers("/emergency-requests/nearby")
                                    .hasAnyRole("ADMIN", "TECHNICIAN")
                                .requestMatchers("/emergency-requests/estimated-price")
                                    .hasAnyRole("ADMIN", "USER")
                                .requestMatchers(
                                        "/emergency-requests/{id}/accept",
                                        "/emergency-requests/{id}/cancel-responder",
                                        "/emergency-requests/{id}/in-progress",
                                        "/emergency-requests/{id}/complete").hasAnyRole("ADMIN", "TECHNICIAN")
                                .requestMatchers("/emergency-requests/{id}/cancel")
                                    .hasAnyRole("ADMIN", "USER")
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
