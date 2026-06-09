package com.paytrack.notification.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String successMethod(String input) {
        return "Hello " + input;
    }

    public void exceptionMethod() {
        throw new RuntimeException("Test Exception");
    }
}