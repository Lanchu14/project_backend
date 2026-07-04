package com.paytrack.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paytrack.auth.dto.AdminDashboardResponse;
import com.paytrack.auth.dto.AdminUserResponse;
import com.paytrack.auth.entity.Role;
import com.paytrack.auth.entity.User;
import com.paytrack.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final String USER_NOT_FOUND =
            "User not found";

    private final UserRepository repository;

    /* ================= DASHBOARD ================= */

    @Override
    public AdminDashboardResponse getDashboard() {

        long totalUsers = repository.count();

        long activeUsers =
                repository.countByActiveTrue();

        long inactiveUsers =
                repository.countByActiveFalse();

        long totalAdmins =
                repository.countByRole(Role.ROLE_ADMIN);

        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .totalAdmins(totalAdmins)
                .build();
    }

    /* ================= GET ALL USERS ================= */

    @Override
    public List<AdminUserResponse> getAllUsers() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /* ================= ACTIVATE USER ================= */

    @Override
    public void activateUser(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                USER_NOT_FOUND));

        user.setActive(true);

        repository.save(user);
    }

    /* ================= DEACTIVATE USER ================= */

    @Override
    public void deactivateUser(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                USER_NOT_FOUND));

        user.setActive(false);

        repository.save(user);
    }

    /* ================= MAPPER ================= */

    private AdminUserResponse mapToResponse(User user) {

        return AdminUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.isActive())
                .build();
    }

}