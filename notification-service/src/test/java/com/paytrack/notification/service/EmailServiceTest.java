package com.paytrack.notification.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ TEST: email sent correctly
    @Test
    void shouldSendEmailSuccessfully() {

        String to = "test@example.com";
        String subject = "Test Subject";
        String message = "Hello from PayTrack";

        emailService.sendEmail(to, subject, message);

        // capture the mail object
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMail = messageCaptor.getValue();

        assertEquals(to, sentMail.getTo()[0]);
        assertEquals(subject, sentMail.getSubject());
        assertEquals(message, sentMail.getText());
    }

    // ❌ TEST: empty values (edge case)
    @Test
    void shouldHandleEmptyValues() {

        emailService.sendEmail("", "", "");

        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMail = messageCaptor.getValue();

        assertEquals("", sentMail.getTo()[0]);
        assertEquals("", sentMail.getSubject());
        assertEquals("", sentMail.getText());
    }

    // ❌ TEST: null values
    @Test
    void shouldHandleNullValues() {

        emailService.sendEmail(null, null, null);

        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMail = messageCaptor.getValue();

        assertNull(sentMail.getTo()[0]);
        assertNull(sentMail.getSubject());
        assertNull(sentMail.getText());
    }
}