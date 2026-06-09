package com.paytrack.report.security;

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

        // 🔐 raw secret (must be long enough for HMAC)
        rawSecret = "mysecretkeymysecretkeymysecretkey123";

        // convert to Base64 (because your code expects Base64)
        base64Secret = Base64.getEncoder().encodeToString(rawSecret.getBytes());

        // inject secret into JwtUtil
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
        String token = generateToken("john", 100000);

        String username = jwtUtil.extractUsername(token);

        assertEquals("john", username);
    }

    // ✅ TEST 2: valid token
    @Test
    void shouldValidateToken_whenValid() {
        String token = generateToken("john", 100000);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    // ❌ TEST 3: expired token
    @Test
    void shouldReturnFalse_whenTokenExpired() {
        String token = generateToken("john", -1000); // already expired

        boolean isValid = jwtUtil.validateToken(token);

        assertFalse(isValid);
    }

    // ❌ TEST 4: invalid token string
    @Test
    void shouldReturnFalse_whenTokenInvalid() {
        String invalidToken = "invalid.token.value";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }

    // ❌ TEST 5: malformed token (exception case)
    @Test
    void shouldThrowException_whenExtractUsernameInvalidToken() {
        String invalidToken = "abc";

        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(invalidToken);
        });
    }
}