package com.paytrack.user.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.*;

import static org.mockito.Mockito.*;

class JwtFilterTest {

    @Test
    void testFilter() throws Exception {

        JwtUtil jwtUtil = mock(JwtUtil.class);

        JwtFilter filter = new JwtFilter(jwtUtil);

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.addHeader("Authorization",
                "Bearer token");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        FilterChain chain =
                mock(FilterChain.class);

        when(jwtUtil.extractUsername("token"))
                .thenReturn("test");

        when(jwtUtil.isTokenValid("token"))
                .thenReturn(true);

        filter.doFilterInternal(
                request,
                response,
                chain
        );

        verify(chain, times(1))
                .doFilter(request, response);
    }
}