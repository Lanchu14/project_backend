package com.paytrack.expense.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

class JwtFilterTest {

    @Test
    void testFilter() throws Exception {

        JwtUtil jwtUtil = mock(JwtUtil.class);

        JwtFilter filter =
                new JwtFilter(jwtUtil);

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        HttpServletResponse response =
                mock(HttpServletResponse.class);

        FilterChain chain =
                mock(FilterChain.class);

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");

        when(jwtUtil.extractUsername("token"))
                .thenReturn("testuser");

        when(jwtUtil.isTokenValid("token"))
                .thenReturn(true);

        filter.doFilterInternal(
                request,
                response,
                chain
        );

        verify(chain)
                .doFilter(request, response);

        SecurityContextHolder.clearContext();
    }
}