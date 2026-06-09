package com.paytrack.expense.dto;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private Double monthlyBudget;
    private Double remainingBudget;
}