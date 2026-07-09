package com.paytrack.notification.dto;

import lombok.Data;

@Data
public class UserDeactivationMessage {

    private String email;

    private String username;

    private String adminEmail;
}