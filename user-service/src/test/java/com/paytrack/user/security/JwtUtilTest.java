package com.paytrack.user.security;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secret;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        //  Base64 secret (must be long enough)
        secret = "mysecretkeymysecretkeymysecretkey12";

        // Inject secret using reflection
        Field field = JwtUtil.class.getDeclaredField("secret");
        field.setAccessible(true);
        field.set(jwtUtil, java.util.Base64.getEncoder().encodeToString(secret.getBytes()));
    }

    private String generateToken(String username, String role, Date expiry) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    //  TEST: extractUsername
    @Test
    void testExtractUsername() {
        String token = generateToken("john", "USER",
                new Date(System.currentTimeMillis() + 100000));

        String username = jwtUtil.extractUsername(token);

        assertEquals("john", username);
    }

    //  TEST: extractRole
    @Test
    void testExtractRole() {
        String token = generateToken("john", "ADMIN",
                new Date(System.currentTimeMillis() + 100000));

        String role = jwtUtil.extractRole(token);

        assertEquals("ADMIN", role);
    }

    // ✅ TEST: valid token
    @Test
    void testTokenValid() {
        String token = generateToken("john", "USER",
                new Date(System.currentTimeMillis() + 100000));

        assertTrue(jwtUtil.isTokenValid(token));
    }

    //  TEST: expired token
    @Test
    void testTokenExpired() {
        String token = generateToken("john", "USER",
                new Date(System.currentTimeMillis() - 1000));

        assertFalse(jwtUtil.isTokenValid(token));
    }
}