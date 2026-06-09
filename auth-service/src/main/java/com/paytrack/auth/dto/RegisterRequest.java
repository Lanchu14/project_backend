package com.paytrack.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(
            message = "Username is required"
    )

    @Size(
            min = 4,
            max = 20,
            message = "Username must be 4 to 20 characters"
    )

    @Pattern(
            regexp = "^\\w+$",
            message =
                    "Username can contain only letters, numbers and underscore"
    )
    private String username;

    @Email(
            message = "Invalid email format"
    )

    @NotBlank(
            message = "Email is required"
    )
    private String email;

    @NotBlank(
            message = "Password is required"
    )

    @Pattern(
            regexp =
                    "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",

            message =
                    "Password must contain 8 characters, uppercase, lowercase, number and special character"
    )
    private String password;
}