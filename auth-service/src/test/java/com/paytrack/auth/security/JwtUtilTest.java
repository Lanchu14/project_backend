package com.paytrack.auth.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void testGenerateToken(){

        JwtUtil jwtUtil = new JwtUtil();

        ReflectionTestUtils.setField(jwtUtil,"secret",
                "VGhpcy1pcy1hLXNlY3JldC1rZXktZm9yLXRlc3Rpbmc=");
        ReflectionTestUtils.setField(jwtUtil,"expiration",100000);

        String token = jwtUtil.generateToken("test","USER");

        assertNotNull(token);
    }

    @Test
    void testExtractUsername(){

        JwtUtil jwtUtil = new JwtUtil();

        ReflectionTestUtils.setField(jwtUtil,"secret",
                "VGhpcy1pcy1hLXNlY3JldC1rZXktZm9yLXRlc3Rpbmc=");
        ReflectionTestUtils.setField(jwtUtil,"expiration",100000);

        String token = jwtUtil.generateToken("test","USER");

        String username = jwtUtil.extractUsername(token);

        assertEquals("test", username);
    }

}