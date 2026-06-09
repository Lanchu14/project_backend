package com.paytrack.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token;

    private boolean forcePasswordReset;

    // ROLE FOR FRONTEND ROLE-BASED LOGIN
    private String role;
}