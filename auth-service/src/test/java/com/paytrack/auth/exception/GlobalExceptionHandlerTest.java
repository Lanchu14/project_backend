package com.paytrack.auth.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void testHandleValidationExceptions() {

        // Mock BindingResult
        BindingResult bindingResult =
                mock(BindingResult.class);

        FieldError fieldError =
                new FieldError(
                        "registerRequest",
                        "password",
                        "Password should contain uppercase, lowercase, number and special character"
                );

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(
                        null,
                        bindingResult
                );

        ResponseEntity<Map<String, String>> response =
                handler.handleValidationExceptions(
                        exception
                );

        // Correct Status Assertion
        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        // Correct Body Assertion
        assertEquals(
                "Password should contain uppercase, lowercase, number and special character",
                response.getBody().get("password")
        );
    }
}