package com.paytrack.user.dto;

import lombok.Data;

@Data
public class UserCreateRequest {

    private String username;
    private String email;

}