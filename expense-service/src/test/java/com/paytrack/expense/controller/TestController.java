package com.paytrack.expense.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping
    public String test(@RequestBody TestRequest request) {
        return "success";
    }

    static class TestRequest {

        @NotBlank(message = "Name is required")
        public String name;
    }
}