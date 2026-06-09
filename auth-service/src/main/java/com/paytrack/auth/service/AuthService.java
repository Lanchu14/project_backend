package com.paytrack.auth.service;

import com.paytrack.auth.dto.*;
import com.paytrack.auth.entity.User;

public interface AuthService {

    String register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    User getByUsername(String username);

    String forgotPassword(String email);

    String resetPassword(String username, ResetPasswordRequest request);
}