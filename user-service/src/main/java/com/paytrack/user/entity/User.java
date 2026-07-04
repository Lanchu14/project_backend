package com.paytrack.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

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
    @Positive(message = "Monthly Budget must be greater than zero.")
    private Double monthlyBudget;

    private Double remainingBudget;

    @NotNull
    @PositiveOrZero(message = "Savings Goal cannot be negative.")
    private Double savingsGoal;

    @Column(nullable = false)
    @Builder.Default
    private boolean profileCompleted = false;
}