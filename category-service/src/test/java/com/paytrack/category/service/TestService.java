package com.paytrack.category.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String successMethod(String name) {
        return "Hello " + name;
    }

    public void exceptionMethod() {
        throw new RuntimeException("Test Exception");
    }
}