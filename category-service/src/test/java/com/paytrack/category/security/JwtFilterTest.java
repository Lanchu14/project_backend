package com.paytrack.category.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

class JwtFilterTest {

    @Test
    void testDoFilterInternal() throws Exception {

        JwtUtil jwtUtil = mock(JwtUtil.class);

        JwtFilter jwtFilter =
                new JwtFilter(jwtUtil);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        FilterChain filterChain =
                mock(FilterChain.class);

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");

        when(jwtUtil.extractUsername("token"))
                .thenReturn("testuser");

        when(jwtUtil.isTokenValid("token"))
                .thenReturn(true);

        jwtFilter.doFilterInternal(
                request,
                response,
                filterChain
        );

        verify(filterChain)
                .doFilter(request, response);

        SecurityContextHolder.clearContext();
    }
}