package com.paytrack.notification.aspect;

import static org.junit.jupiter.api.Assertions.*;

import com.paytrack.notification.service.TestService;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;




@SpringBootTest
class LoggingAspectTest {

    @Autowired
    private TestService testService;

    @Test
    void shouldLogBeforeAndAfter() {

        // capture logs
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        // call method
        String result = testService.successMethod("John");

        assertEquals("Hello John", result);

        // verify logs
        boolean beforeLog = listAppender.list.stream()
                .anyMatch(log -> log.getFormattedMessage().contains("ENTER"));

        boolean afterLog = listAppender.list.stream()
                .anyMatch(log -> log.getFormattedMessage().contains("EXIT"));

        assertTrue(beforeLog);
        assertTrue(afterLog);
    }

    @Test
    void shouldLogException() {

        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        
            testService.exceptionMethod();
        

        boolean errorLog = listAppender.list.stream()
                .anyMatch(log -> log.getFormattedMessage().contains("ERROR"));

        assertTrue(errorLog);
    }
}