package com.paytrack.auth.dto;

import lombok.Data;

@Data
public class UserCreateRequest {

    private String username;
    private String email;

}