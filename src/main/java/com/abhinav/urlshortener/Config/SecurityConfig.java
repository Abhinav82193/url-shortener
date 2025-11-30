package com.abhinav.urlshortener.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            // H2 console safe + local mein chalega
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()      // H2 console
                .requestMatchers("/actuator/**").permitAll()        // monitoring
                .requestMatchers("/api/shorten").permitAll()        // shorten API
                .requestMatchers("/{shortCode:[a-zA-Z0-9_-]+}").permitAll() // redirect
                .anyRequest().denyAll() 
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
