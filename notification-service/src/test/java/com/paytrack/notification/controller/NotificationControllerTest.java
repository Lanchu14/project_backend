package com.paytrack.notification.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.paytrack.notification.service.EmailService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmailService emailService;

    // ✅ TEST 1: success case
    @Test
    void shouldSendEmailSuccessfully() throws Exception {

        mockMvc.perform(post("/notify/email")
                .param("email", "test@example.com")
                .param("subject", "Hello")
                .param("message", "Test Message"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email Sent"));

        verify(emailService, times(1))
                .sendEmail("test@example.com", "Hello", "Test Message");
    }

    // ❌ TEST 2: missing parameter
    @Test
    void shouldFail_whenMissingEmail() throws Exception {

        mockMvc.perform(post("/notify/email")
                .param("subject", "Hello")
                .param("message", "Test Message"))
                .andExpect(status().isBadRequest());

        verify(emailService, never()).sendEmail(any(), any(), any());
    }
}