package com.paytrack.category.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {

        handler = new GlobalExceptionHandler();
    }

    /* ================= ILLEGAL ARGUMENT ================= */

    @Test
    void testHandleIllegalArgument() {

        IllegalArgumentException exception =
                new IllegalArgumentException(
                        "Invalid category"
                );

        ResponseEntity<String> response =
                handler.handleIllegalArgument(
                        exception
                );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        assertEquals(
                "Invalid category",
                response.getBody()
        );
    }

    /* ================= SECURITY EXCEPTION ================= */

    @Test
    void testHandleUnauthorizedWithSecurityException() {

        SecurityException exception =
                new SecurityException(
                        "Unauthorized"
                );

        ResponseEntity<String> response =
                handler.handleUnauthorized(
                        exception
                );

        assertEquals(
                HttpStatus.FORBIDDEN,
                response.getStatusCode()
        );

        assertEquals(
                "Unauthorized",
                response.getBody()
        );
    }

    /* ================= ACCESS DENIED ================= */

    @Test
    void testHandleUnauthorizedWithAccessDeniedException() {

        AccessDeniedException exception =
                new AccessDeniedException(
                        "Access denied"
                );

        ResponseEntity<String> response =
                handler.handleUnauthorized(
                        exception
                );

        assertEquals(
                HttpStatus.FORBIDDEN,
                response.getStatusCode()
        );

        assertEquals(
                "Access denied",
                response.getBody()
        );
    }

    /* ================= GENERAL EXCEPTION ================= */

    @Test
    void testHandleGeneral() {

        Exception exception =
                new Exception(
                        "Internal Error"
                );

        ResponseEntity<String> response =
                handler.handleGeneral(
                        exception
                );

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertEquals(
                "Something went wrong",
                response.getBody()
        );
    }
}