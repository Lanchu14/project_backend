package com.paytrack.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @Pattern(
            regexp = "^\\d{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phone;

    private String city;

    private Double monthlyBudget;

    private Double savingsGoal;

}