package com.paytrack.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.paytrack.auth.security.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth            //it is used so that it can open the request and access it 
            		.requestMatchers(
            		        "/swagger-ui/**",
            		        "/v3/api-docs/**",
            		        "/swagger-ui.html"
            		).permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/error").permitAll()
             // ADMIN ROUTES
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")

                // USER ROUTES
                .requestMatchers("/user/**")
                .hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            //this line tells donot create the session use jwt itself bcoz jwt is stateless
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
//bcoz jwt runs before authentication so we have define this 
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}