package com.paytrack.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @Email
    @NotBlank
    private String email;

    @Pattern(
            regexp = "\\d{10}$",
            message = "Phone must be 10 digits"
    )
    private String phone;

    private String city;

    @NotNull
    private Double monthlyBudget;

    @NotNull
    private Double savingsGoal;
}