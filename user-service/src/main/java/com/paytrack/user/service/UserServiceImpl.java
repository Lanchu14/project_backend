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

    /* ================= UPDATE PROFILE ================= */

    @Override
    public User updateProfile(
            String username,
            User user) {

        User existing =
                repository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        USER_NOT_FOUND
                                ));

        // Update only allowed fields
        if (user.getPhone() != null) {

            existing.setPhone(
                    user.getPhone()
            );
        }

        if (user.getCity() != null) {

            existing.setCity(
                    user.getCity()
            );
        }

        if (user.getMonthlyBudget() != null) {

            existing.setMonthlyBudget(
                    user.getMonthlyBudget()
            );

            // Reset remaining budget
            existing.setRemainingBudget(
                    user.getMonthlyBudget()
            );
        }

        if (user.getSavingsGoal() != null) {

            existing.setSavingsGoal(
                    user.getSavingsGoal()
            );
        }

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