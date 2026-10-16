package com.studentvault.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration   // this class defines beans
public class SecurityConfig {

    // TEMPORARY (Step 7 replaces this with JWT auth). Do not deploy as-is.
    @Bean        // the returned object is registered as a bean Spring uses
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())   // stateless JSON API; JWT won't use cookies
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}