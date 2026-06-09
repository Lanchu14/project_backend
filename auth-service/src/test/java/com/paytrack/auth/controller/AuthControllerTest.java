package com.paytrack.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytrack.auth.dto.ForgotPasswordRequest;
import com.paytrack.auth.dto.LoginRequest;
import com.paytrack.auth.dto.LoginResponse;
import com.paytrack.auth.dto.RegisterRequest;
import com.paytrack.auth.dto.ResetPasswordRequest;
import com.paytrack.auth.entity.User;
import com.paytrack.auth.security.CustomUserDetailsService;
import com.paytrack.auth.security.JwtUtil;
import com.paytrack.auth.service.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService service;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("john");
        registerRequest.setEmail("john@gmail.com");
        registerRequest.setPassword("Password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("Password123");
    }

    @Test
    void testRegister() throws Exception {

        when(service.register(any(RegisterRequest.class)))
                .thenReturn("User Registered Successfully");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("User Registered Successfully"));
    }

    @Test
    void testLogin() throws Exception {

        LoginResponse response =
                new LoginResponse(
                        "jwt-token",
                        false,
                        "ROLE_USER"
                );

        when(service.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("jwt-token"))
                .andExpect(jsonPath("$.passwordResetRequired")
                        .value(false))
                .andExpect(jsonPath("$.role")
                        .value("ROLE_USER"));
    }

    @Test
    void testForgotPassword() throws Exception {

        ForgotPasswordRequest request =
                new ForgotPasswordRequest();

        request.setEmail("john@gmail.com");

        when(service.forgotPassword("john@gmail.com"))
                .thenReturn(
                        "Temporary password sent successfully"
                );

        mockMvc.perform(post("/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Temporary password sent successfully"
                ));
    }

    @Test
    void testResetPassword() throws Exception {

        ResetPasswordRequest request =
                new ResetPasswordRequest();

        request.setEmail("john@gmail.com");
        request.setNewPassword("NewPassword123");

        when(service.resetPassword(
                any(String.class),
                any(ResetPasswordRequest.class)
        )).thenReturn("Password reset successful");

        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("Password reset successful"));
    }

    @Test
    void testGetMyDetails() throws Exception {

        User user = User.builder()
                .username("john")
                .email("john@gmail.com")
                .build();

        when(service.getByUsername("john"))
                .thenReturn(user);

        mockMvc.perform(get("/auth/me")
                        .principal(() -> "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username")
                        .value("john"))
                .andExpect(jsonPath("$.email")
                        .value("john@gmail.com"));
    }
}