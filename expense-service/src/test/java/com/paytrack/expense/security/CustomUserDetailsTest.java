package com.paytrack.expense.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void testCustomUserDetails() {

        CustomUserDetails user =
                new CustomUserDetails("testuser");

        assertEquals("testuser", user.getUsername());
        assertNull(user.getPassword());

        Collection<? extends GrantedAuthority> authorities =
                user.getAuthorities();

        assertFalse(authorities.isEmpty());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }
}