package com.paytrack.expense.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.paytrack.expense.model.User;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

	@PutMapping("/users/internal/deduct")
	User deductBudget(
	        @RequestParam String username,
	        @RequestParam Double amount);

    @GetMapping("/users/username/{username}")
    User getUser(@PathVariable String username);

    @PutMapping("/users/add-budget")
    void addBudget(
            @RequestParam String username,
            @RequestParam Double amount);
    
    
    
}