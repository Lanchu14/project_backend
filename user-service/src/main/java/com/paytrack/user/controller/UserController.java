package com.paytrack.user.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.paytrack.user.dto.UserCreateRequest;
import com.paytrack.user.dto.UserResponse;
import com.paytrack.user.dto.UserUpdateRequest;
import com.paytrack.user.entity.User;
import com.paytrack.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

   

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {

        return service.getAll();
    }

   

    @GetMapping("/me")
    public User getCurrentUser(
            Principal principal) {

        return service.getByUsername(
                principal.getName()
        );
    }

   

    @PutMapping("/update")
    public User update(
            Principal principal,
            @Valid @RequestBody UserUpdateRequest request) {

    	User user = User.builder()
    	        .phone(request.getPhone())
    	        .city(request.getCity())
    	        .monthlyBudget(request.getMonthlyBudget())
    	        .savingsGoal(request.getSavingsGoal())
    	        .build();

        return service.updateProfile(
                principal.getName(),
                user
        );
    }



    @PostMapping("/internal/create")
    public User createInternal(
            @RequestBody UserCreateRequest request) {

        User user = new User();

        user.setUsername(
                request.getUsername()
        );

        user.setEmail(
                request.getEmail()
        );

        // Default values
        user.setPhone("1234567891");
        user.setCity("");
        user.setMonthlyBudget(0.0);
        user.setSavingsGoal(0.0);
        user.setRemainingBudget(0.0);

        return service.create(user);
    }



    @GetMapping("/username/{username}")
    public User getByUsername(
            @PathVariable String username) {

        return service.getByUsername(username);
    }

   

    @PutMapping("/internal/deduct")
    public void deductBudget(
            @RequestParam String username,
            @RequestParam Double amount) {

        service.deductBudget(
                username,
                amount
        );
    }



    @PutMapping("/add-budget")
    public void addBudget(
            @RequestParam String username,
            @RequestParam Double amount) {

        service.addBudget(
                username,
                amount
        );
    }
}