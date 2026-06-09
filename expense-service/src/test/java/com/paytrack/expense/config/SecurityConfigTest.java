package com.paytrack.expense.config;

import com.paytrack.expense.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void testSecurityFilterChain() throws Exception {

        JwtFilter jwtFilter = Mockito.mock(JwtFilter.class);

        SecurityConfig securityConfig =
                new SecurityConfig(jwtFilter);

        HttpSecurity http =
                Mockito.mock(HttpSecurity.class,
                        Mockito.RETURNS_DEEP_STUBS);

        SecurityFilterChain chain =
                securityConfig.securityFilterChain(http);

        assertNotNull(chain);
    }
}