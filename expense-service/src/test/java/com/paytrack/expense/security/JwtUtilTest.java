package com.paytrack.expense.security;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String rawSecret;
    private String base64Secret;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        // 🔐 strong secret (>= 32 chars for HS256)
        rawSecret = "mysecretkeymysecretkeymysecretkey123";

        // convert to Base64 (because JwtUtil expects Base64)
        base64Secret = Base64.getEncoder().encodeToString(rawSecret.getBytes());

        // inject into JwtUtil using reflection
        Field field = JwtUtil.class.getDeclaredField("secret");
        field.setAccessible(true);
        field.set(jwtUtil, base64Secret);
    }

    // 🔧 helper method to generate token
    private String generateToken(String username, long expiryMillis) {
        Key key = Keys.hmacShaKeyFor(rawSecret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiryMillis))
                .signWith(key)
                .compact();
    }

    // ✅ TEST 1: extract username
    @Test
    void shouldExtractUsername() {
        String token = generateToken("john", 60000);

        String username = jwtUtil.extractUsername(token);

        assertEquals("john", username);
    }

    // ✅ TEST 2: valid token
    @Test
    void shouldReturnTrue_whenTokenValid() {
        String token = generateToken("john", 60000);

        assertTrue(jwtUtil.isTokenValid(token));
    }

    // ❌ TEST 3: expired token (fixed timing issue)
    @Test
    void shouldReturnFalse_whenTokenExpired() {
        String token = generateToken("john", -10000); // 🔥 FIX (bigger gap)

        assertFalse(jwtUtil.isTokenValid(token));
    }

    // ❌ TEST 4: invalid token format
    @Test
    void shouldThrowException_whenInvalidToken() {
        String invalidToken = "invalid.token";

        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(invalidToken);
        });
    }
}