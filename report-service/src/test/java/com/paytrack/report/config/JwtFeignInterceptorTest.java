package com.paytrack.report.config;

import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;

class JwtFeignInterceptorTest {

    @Test
    void testApply() {

        JwtFeignInterceptor interceptor =
                new JwtFeignInterceptor();

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.addHeader("Authorization", "Bearer test");

        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(request));

        RequestTemplate template =
                new RequestTemplate();

        interceptor.apply(template);

        assertTrue(
                template.headers()
                        .containsKey("Authorization"));
    }
}