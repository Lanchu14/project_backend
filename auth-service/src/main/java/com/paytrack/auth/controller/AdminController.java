package com.paytrack.auth.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.paytrack.auth.dto.AdminDashboardResponse;
import com.paytrack.auth.dto.AdminUserResponse;
import com.paytrack.auth.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    /* ================= DASHBOARD ================= */

    @GetMapping("/dashboard")
    public AdminDashboardResponse dashboard() {

        return adminService.getDashboard();
    }

    /* ================= GET ALL USERS ================= */

    @GetMapping("/users")
    public List<AdminUserResponse> getAllUsers() {

        return adminService.getAllUsers();
    }

    /* ================= ACTIVATE USER ================= */

    @PutMapping("/users/{id}/activate")
    public String activateUser(
            @PathVariable Long id) {

        adminService.activateUser(id);

        return "User Activated Successfully";
    }

    /* ================= DEACTIVATE USER ================= */

    @PutMapping("/users/{id}/deactivate")
    public String deactivateUser(
            @PathVariable Long id) {

        adminService.deactivateUser(id);

        return "User Deactivated Successfully";
    }

}