package com.paytrack.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paytrack.auth.dto.AdminUserResponse;
import com.paytrack.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository repository;

    @GetMapping("/users")
    public List<AdminUserResponse> getAllUsers() {

        return repository.findAll()
                .stream()
                .map(user -> new AdminUserResponse(
                        user.getUsername(),
                        user.getEmail()
                ))
                .toList();
    }
}