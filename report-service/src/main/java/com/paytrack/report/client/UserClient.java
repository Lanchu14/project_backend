package com.paytrack.report.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.paytrack.report.model.User;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @GetMapping("/users/username/{username}")
    User getUser(@PathVariable String username);
}