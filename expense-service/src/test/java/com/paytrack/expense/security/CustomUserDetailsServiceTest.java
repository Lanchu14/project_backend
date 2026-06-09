package com.paytrack.expense.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsServiceTest {

    private final CustomUserDetailsService service =
            new CustomUserDetailsService();

    @Test
    void testLoadUserByUsername() {

        UserDetails user =
                service.loadUserByUsername("testuser");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }
}