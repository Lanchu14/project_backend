package com.paytrack.user.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void testUserDetails(){

        CustomUserDetails user =
                new CustomUserDetails("test");

        assertEquals("test", user.getUsername());
        assertTrue(user.isEnabled());
    }
}