package com.paytrack.auth.security;

import jakarta.servlet.FilterChain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {

        SecurityContextHolder.clearContext();
    }

   

    @Test
    void testShouldNotFilterLogin() {

        when(request.getServletPath())
                .thenReturn("/auth/login");

        boolean result =
                jwtFilter.shouldNotFilter(request);

        assertTrue(result);
    }

    @Test
    void testShouldNotFilterRegister() {

        when(request.getServletPath())
                .thenReturn("/auth/register");

        boolean result =
                jwtFilter.shouldNotFilter(request);

        assertTrue(result);
    }

    @Test
    void testShouldFilterProtectedRoute() {

        when(request.getServletPath())
                .thenReturn("/expenses");

        boolean result =
                jwtFilter.shouldNotFilter(request);

        assertFalse(result);
    }

    

    @Test
    void testDoFilterInternalValidToken()
            throws Exception {

        String token = "jwt-token";
        String username = "john";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtUtil.extractUsername(token))
                .thenReturn(username);

        User userDetails =
                new User(
                        username,
                        "password",
                        Collections.emptyList()
                );

        when(userDetailsService
                .loadUserByUsername(username))
                .thenReturn(userDetails);

        when(jwtUtil.isTokenValid(token))
                .thenReturn(true);

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(jwtUtil)
                .extractUsername(token);

        verify(userDetailsService)
                .loadUserByUsername(username);

        verify(jwtUtil)
                .isTokenValid(token);

        verify(filterChain)
                .doFilter(request, response);

        assertTrue(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        != null
        );
    }

    

    @Test
    void testDoFilterInternalInvalidToken()
            throws Exception {

        String token = "invalid-token";
        String username = "john";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtUtil.extractUsername(token))
                .thenReturn(username);

        User userDetails =
                new User(
                        username,
                        "password",
                        Collections.emptyList()
                );

        when(userDetailsService
                .loadUserByUsername(username))
                .thenReturn(userDetails);

        when(jwtUtil.isTokenValid(token))
                .thenReturn(false);

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(filterChain)
                .doFilter(request, response);

        assertTrue(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        == null
        );
    }

   

    @Test
    void testDoFilterInternalNoAuthHeader()
            throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(filterChain)
                .doFilter(request, response);

        verifyNoInteractions(jwtUtil);

        assertTrue(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        == null
        );
    }
}