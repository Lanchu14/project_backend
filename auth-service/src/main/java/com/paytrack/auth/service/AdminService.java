package com.paytrack.auth.service;

import java.util.List;

import com.paytrack.auth.dto.AdminDashboardResponse;
import com.paytrack.auth.dto.AdminUserResponse;

public interface AdminService {


    AdminDashboardResponse getDashboard();

    List<AdminUserResponse> getAllUsers();

    void activateUser(Long id);

    void deactivateUser(Long id);

}