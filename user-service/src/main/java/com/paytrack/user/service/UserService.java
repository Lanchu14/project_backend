package com.paytrack.user.service;

import java.util.List;

import com.paytrack.user.dto.UserResponse;
import com.paytrack.user.entity.User;

public interface UserService {

    User create(User user);

    List<UserResponse> getAll();

    User getByUsername(String username);

    User updateProfile(String username, User user);

    User deductBudget(String username, Double amount);

    void addBudget(String username, Double amount);
}