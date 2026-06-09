package com.paytrack.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void testRuntimeException(){

        RuntimeException ex =
                new RuntimeException("User not found");

        String response =
                handler.handleRuntime(ex);

        assertEquals("User not found", response);
    }

    @Test
    void testValidationException(){

        BindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "object");

        bindingResult.addError(
                new FieldError("object",
                        "username",
                        "Username required"));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        Map<String,String> result =
                handler.handleValidation(ex);

        assertEquals("Username required",
                result.get("username"));
    }
}