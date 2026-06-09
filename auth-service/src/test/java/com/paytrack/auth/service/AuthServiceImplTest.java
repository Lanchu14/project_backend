package com.paytrack.auth.service;

import com.paytrack.auth.client.UserClient;
import com.paytrack.auth.dto.*;
import com.paytrack.auth.entity.Role;
import com.paytrack.auth.entity.User;
import com.paytrack.auth.repository.UserRepository;
import com.paytrack.auth.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserClient userClient;

    @Mock
    private ForgotPasswordProducer forgotPasswordProducer;

    @InjectMocks
    private AuthServiceImpl service;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("john");
        registerRequest.setEmail("john@gmail.com");
        registerRequest.setPassword("Password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("Password123");

        user = User.builder()
                .username("john")
                .email("john@gmail.com")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .passwordResetRequired(false)
                .build();
    }

    /* ================= REGISTER ================= */

    @Test
    void testRegisterSuccess() {

        when(repository.findByUsername("john"))
                .thenReturn(Optional.empty());

        when(repository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.empty());

        when(encoder.encode("Password123"))
                .thenReturn("encodedPassword");

        String result = service.register(registerRequest);

        assertEquals(
                "User Registered Successfully",
                result
        );

        verify(repository).save(any(User.class));
        verify(userClient).createUser(any(UserCreateRequest.class));
    }

    @Test
    void testRegisterUsernameExists() {

        when(repository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register(registerRequest)
        );

        assertEquals(
                "Username already exists",
                exception.getMessage()
        );
    }

    /* ================= LOGIN ================= */

    @Test
    void testLoginSuccess() {

        when(repository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(encoder.matches(
                "Password123",
                "encodedPassword"))
                .thenReturn(true);

        when(jwtUtil.generateToken(
                "john",
                "ROLE_USER"))
                .thenReturn("jwt-token");

        LoginResponse response =
                service.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("ROLE_USER", response.getRole());
    }

    @Test
    void testLoginInvalidPassword() {

        when(repository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(encoder.matches(any(), any()))
                .thenReturn(false);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.login(loginRequest)
        );

        assertEquals(
                "Invalid Credentials",
                exception.getMessage()
        );
    }

    @Test
    void testLoginTempPasswordExpired() {

        user.setPasswordResetRequired(true);

        user.setTempPasswordExpiry(
                LocalDateTime.now().minusMinutes(5)
        );

        when(repository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(encoder.matches(any(), any()))
                .thenReturn(true);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.login(loginRequest)
        );

        assertEquals(
                "Temporary password expired. Please request again.",
                exception.getMessage()
        );
    }

    /* ================= FORGOT PASSWORD ================= */

    @Test
    void testForgotPasswordSuccess() {

        when(repository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        when(encoder.encode(any()))
                .thenReturn("tempEncodedPassword");

        String result =
                service.forgotPassword("john@gmail.com");

        assertEquals(
                "Temporary password sent successfully",
                result
        );

        verify(repository).save(any(User.class));

        verify(forgotPasswordProducer)
                .sendMessage(any(String.class));
    }

    @Test
    void testForgotPasswordEmailNotFound() {

        when(repository.findByEmail("abc@gmail.com"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.forgotPassword("abc@gmail.com")
        );

        assertEquals(
                "Email not found",
                exception.getMessage()
        );
    }

    /* ================= RESET PASSWORD ================= */

    @Test
    void testResetPasswordSuccess() {

        ResetPasswordRequest request =
                new ResetPasswordRequest();

        request.setNewPassword("NewPassword123");

        when(repository.findByEmail("john@gmail.com"))
                .thenReturn(Optional.of(user));

        when(encoder.encode("NewPassword123"))
                .thenReturn("newEncodedPassword");

        String result = service.resetPassword(
                "john@gmail.com",
                request
        );

        assertEquals(
                "Password reset successful",
                result
        );

        verify(repository).save(user);
    }

    /* ================= GET USER ================= */

    @Test
    void testGetByUsernameSuccess() {

        when(repository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        User result = service.getByUsername("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
    }

    @Test
    void testGetByUsernameNotFound() {

        when(repository.findByUsername("john"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> service.getByUsername("john")
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );
    }
}