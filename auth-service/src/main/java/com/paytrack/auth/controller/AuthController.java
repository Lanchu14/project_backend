package com.paytrack.auth.controller;


import com.paytrack.auth.dto.*;
import com.paytrack.auth.entity.User;
import com.paytrack.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(service.forgotPassword(request.getEmail()));
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {

        return service.resetPassword(request.getEmail(), request);
    }


    
    @GetMapping("/me")
    public UserResponse getMyDetails(Principal principal){

        User user = service.getByUsername(principal.getName());

        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
 
}