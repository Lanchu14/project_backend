package com.paytrack.auth.dto;

import lombok.Data;

@Data
public class UserProfileRequest {

    private String username;
    private String email;

}