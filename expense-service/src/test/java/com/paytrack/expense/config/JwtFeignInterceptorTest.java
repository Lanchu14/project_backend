package com.paytrack.expense.config;

import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtFeignInterceptorTest {

    @Test
    void testApply_WithAuthorizationHeader() {

        JwtFeignInterceptor interceptor =
                new JwtFeignInterceptor();

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");

        ServletRequestAttributes attributes =
                new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);

        RequestTemplate template =
                new RequestTemplate();

        interceptor.apply(template);

        assertTrue(
                template.headers()
                        .containsKey("Authorization")
        );

        RequestContextHolder.resetRequestAttributes();
    }


    @Test
    void testApply_NoAuthorizationHeader() {

        JwtFeignInterceptor interceptor =
                new JwtFeignInterceptor();

        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        ServletRequestAttributes attributes =
                new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);

        RequestTemplate template =
                new RequestTemplate();

        interceptor.apply(template);

        assertFalse(
                template.headers()
                        .containsKey("Authorization")
        );

        RequestContextHolder.resetRequestAttributes();
    }
}