package com.paytrack.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paytrack.user.dto.UserResponse;
import com.paytrack.user.entity.User;
import com.paytrack.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND =
            "User not found";

    private final UserRepository repository;

    /* ================= CREATE USER ================= */

    @Override
    public User create(User user) {

        return repository.save(user);
    }

    /* ================= GET ALL USERS ================= */

    @Override
    public List<UserResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .toList();
    }

    /* ================= GET USERNAME ================= */

    @Override
    public User getByUsername(String username) {

        return repository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                USER_NOT_FOUND
                        ));
    }

    @Override
    public User updateProfile(String username, User user) {

        User existing = repository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(USER_NOT_FOUND));

        // Phone
        if (user.getPhone() != null) {

            if (!user.getPhone().matches("\\d{10}")) {

                throw new RuntimeException(
                        "Phone number must contain exactly 10 digits."
                );

            }

            existing.setPhone(user.getPhone());

        }

        // City
        if (user.getCity() != null) {

            if (user.getCity().trim().isEmpty()) {

                throw new RuntimeException(
                        "City cannot be empty."
                );

            }

            existing.setCity(user.getCity().trim());

        }

        // Budget
        if (user.getMonthlyBudget() != null) {

            if (user.getMonthlyBudget() < 0) {

                throw new RuntimeException(
                        "Monthly Budget cannot be negative."
                );

            }

            Double spent =
                    existing.getMonthlyBudget()
                            - existing.getRemainingBudget();

            if (user.getMonthlyBudget() < spent) {

                throw new RuntimeException(
                        "Budget cannot be less than current expenses."
                );

            }

            existing.setMonthlyBudget(
                    user.getMonthlyBudget()
            );

            existing.setRemainingBudget(
                    user.getMonthlyBudget() - spent
            );

        }

        // Savings Goal
        if (user.getSavingsGoal() != null) {

            if (user.getSavingsGoal() < 0) {

                throw new RuntimeException(
                        "Savings Goal cannot be negative."
                );

            }

            Double budget = user.getMonthlyBudget() != null
                    ? user.getMonthlyBudget()
                    : existing.getMonthlyBudget();

            if (user.getSavingsGoal() > budget) {

                throw new RuntimeException(
                        "Savings Goal cannot be greater than Monthly Budget."
                );

            }

            existing.setSavingsGoal(
                    user.getSavingsGoal()
            );

        }

        existing.setProfileCompleted(true);

        return repository.save(existing);

    }
    /* ================= DEDUCT BUDGET ================= */

    @Override
    public User deductBudget(
            String username,
            Double amount) {

        User user =
                repository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        USER_NOT_FOUND
                                ));

        Double remaining =
                Math.max(
                        0,
                        user.getRemainingBudget()
                                - amount
                );

        user.setRemainingBudget(
                remaining
        );

        return repository.save(user);
    }

    /* ================= ADD BUDGET ================= */

    @Override
    public void addBudget(
            String username,
            Double amount) {

        User user =
                repository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        USER_NOT_FOUND
                                ));

        user.setRemainingBudget(
                user.getRemainingBudget()
                        + amount
        );

        repository.save(user);
    }
}