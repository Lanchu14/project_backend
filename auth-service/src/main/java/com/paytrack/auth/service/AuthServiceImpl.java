package com.paytrack.auth.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.paytrack.auth.client.UserClient;
import com.paytrack.auth.dto.LoginRequest;
import com.paytrack.auth.dto.LoginResponse;
import com.paytrack.auth.dto.RegisterRequest;
import com.paytrack.auth.dto.ResetPasswordRequest;
import com.paytrack.auth.dto.UserCreateRequest;
import com.paytrack.auth.entity.Role;
import com.paytrack.auth.entity.User;
import com.paytrack.auth.repository.UserRepository;
import com.paytrack.auth.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    /* ================= CONSTANTS ================= */

    private static final String USER_NOT_FOUND =
            "User not found";

    private static final String INVALID_CREDENTIALS =
            "Invalid Credentials";

    private static final String TEMP_PASSWORD_EXPIRED =
            "Temporary password expired. Please request again.";

    private static final String USERNAME_EXISTS =
            "Username already exists";

    private static final String EMAIL_EXISTS =
            "Email already exists";

    private static final String EMAIL_NOT_FOUND =
            "Email not found";

    // Reusable SecureRandom
    private static final SecureRandom RANDOM =
            new SecureRandom();
    

    /* ================= DEPENDENCIES ================= */

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final UserClient userClient;

    // RabbitMQ Producer
    private final ForgotPasswordProducer forgotPasswordProducer;

    /* ================= CONFIG ================= */

    @Value("${app.reset-password-expiry}")
    private int expiryMinutes;

    /* ================= REGISTER ================= */

    @Override
    public String register(RegisterRequest request) {

        if (repository.findByUsername(
                request.getUsername()).isPresent()) {

            throw new IllegalArgumentException(
                    USERNAME_EXISTS);
        }

        if (repository.findByEmail(
                request.getEmail()).isPresent()) {

            throw new IllegalArgumentException(
                    EMAIL_EXISTS);
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(
                        encoder.encode(
                                request.getPassword()
                        )
                )

                // ROLE BASED LOGIN
                .role(Role.ROLE_USER)

                .passwordResetRequired(false)
                .active(true)
                .lastLogin(null)
                .build();

        repository.save(user);

        // Create profile in user-service
        UserCreateRequest userRequest =
                new UserCreateRequest();

        userRequest.setUsername(
                user.getUsername());

        userRequest.setEmail(
                user.getEmail());

        userClient.createUser(userRequest);

        return "User Registered Successfully";
    }

    /* ================= LOGIN ================= */

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException(USER_NOT_FOUND));

        /*
         * USER ACCOUNT STATUS CHECK
         */
        if (!user.isActive()) {

            throw new IllegalArgumentException(
                    "Your account has been deactivated by the administrator. Please contact the administrator for activation."
            );
        }

        /*
         * PASSWORD CHECK
         */
        if (!encoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                    INVALID_CREDENTIALS);
        }

        /*
         * TEMP PASSWORD EXPIRY CHECK
         */
        if (user.isPasswordResetRequired()
                && user.getTempPasswordExpiry() != null
                && user.getTempPasswordExpiry()
                        .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    TEMP_PASSWORD_EXPIRED);
        }

        /*
         * SAVE LAST LOGIN TIME
         */
        user.setLastLogin(LocalDateTime.now());

        repository.save(user);

        /*
         * GENERATE JWT TOKEN
         */
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name());

        return new LoginResponse(
                token,
                user.isPasswordResetRequired(),
                user.getRole().name());
    }

    /* ================= FORGOT PASSWORD ================= */

    @Override
    public String forgotPassword(String email) {

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                EMAIL_NOT_FOUND));

        String tempPassword =
                generateTempPassword();

        user.setPassword(
                encoder.encode(tempPassword));

        user.setPasswordResetRequired(true);

        // EXPIRY TIME
        user.setTempPasswordExpiry(
                LocalDateTime.now()
                        .plusMinutes(expiryMinutes)
        );

        repository.save(user);

        // RabbitMQ Message
        forgotPasswordProducer.sendMessage(
                "FORGOT PASSWORD REQUEST\n"
                        + "Email: "
                        + user.getEmail()
                        + "\nTemporary Password: "
                        + tempPassword
                        + "\nExpiry Time: "
                        + expiryMinutes
                        + " minutes"
        );

        return "Temporary password sent successfully";
    }

    /* ================= RESET PASSWORD ================= */

    @Override
    public String resetPassword(
            String email,
            ResetPasswordRequest request) {

        User user = repository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                USER_NOT_FOUND));

        user.setPassword(
                encoder.encode(
                        request.getNewPassword()
                )
        );

        user.setPasswordResetRequired(false);

        // CLEAR EXPIRY
        user.setTempPasswordExpiry(null);

        repository.save(user);

        return "Password reset successful";
    }

    /* ================= GET USER ================= */

    @Override
    public User getByUsername(String username) {

        return repository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                USER_NOT_FOUND));
    }

    /* ================= TEMP PASSWORD GENERATOR ================= */

    private String generateTempPassword() {

        String upper =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        String lower =
                "abcdefghijklmnopqrstuvwxyz";

        String number =
                "0123456789";

        String special =
                "@#$%^&+=";

        return ""
                + upper.charAt(
                        RANDOM.nextInt(
                                upper.length()))

                + lower.charAt(
                        RANDOM.nextInt(
                                lower.length()))

                + number.charAt(
                        RANDOM.nextInt(
                                number.length()))

                
                + special.charAt(
                        RANDOM.nextInt(
                                special.length()))

                + "Temp123";
    }
}