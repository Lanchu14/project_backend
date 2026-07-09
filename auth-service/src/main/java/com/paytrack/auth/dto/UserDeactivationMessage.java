package com.paytrack.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeactivationMessage {

    private String email;

    private String username;

    private String adminEmail;
}