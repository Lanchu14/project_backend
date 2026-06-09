package com.paytrack.auth.security;

import com.paytrack.auth.entity.User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void testUserDetails(){

        User user = new User();
        user.setUsername("test");
        user.setPassword("pass");

        CustomUserDetails details =
                new CustomUserDetails(user);

        assertEquals("test", details.getUsername());
        assertEquals("pass", details.getPassword());
        assertTrue(details.isAccountNonExpired());
    }
}