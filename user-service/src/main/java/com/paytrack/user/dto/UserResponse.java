package com.paytrack.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private String phone;

    private String city;

    private Double monthlyBudget;

    private Double remainingBudget;

    private Double savingsGoal;

    private boolean profileCompleted;

}