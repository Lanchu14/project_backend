package com.paytrack.expense.model;

import lombok.Data;

@Data
public class User {

    private String username;
    private Double monthlyBudget;
    private Double remainingBudget;
    private String email; 
    private Double savingsGoal;
}