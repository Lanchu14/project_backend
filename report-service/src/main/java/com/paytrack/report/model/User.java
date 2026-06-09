package com.paytrack.report.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private Double monthlyBudget;
    private Double remainingBudget;
}