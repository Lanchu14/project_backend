package com.paytrack.auth.aspect;

import static org.junit.jupiter.api.Assertions.*;

import com.paytrack.auth.service.TestService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

@SpringBootTest
class LoggingAspectTest {

    @Autowired
    private TestService testService;

    // ✅ TEST 1: Before + After logs
    @Test
    void shouldLogBeforeAndAfter() {

        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        String result = testService.successMethod("John");

        assertEquals("Hello John", result);

        boolean hasEnterLog = listAppender.list.stream()
                .anyMatch(log -> log.getFormattedMessage().contains("ENTER"));

        boolean hasExitLog = listAppender.list.stream()
                .anyMatch(log -> log.getFormattedMessage().contains("EXIT"));

        assertTrue(hasEnterLog);
        assertTrue(hasExitLog);
    }

    // ❌ TEST 2: Exception logging
    @Test
    void shouldLogException() {

        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        
            testService.exceptionMethod();
       

        boolean hasErrorLog = listAppender.list.stream()
                .anyMatch(log -> log.getFormattedMessage().contains("ERROR"));

        assertTrue(hasErrorLog);
    }
}