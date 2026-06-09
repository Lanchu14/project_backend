package com.paytrack.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.paytrack.auth.dto.UserCreateRequest;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @PostMapping("/users/internal/create")
    void createUser(@RequestBody UserCreateRequest request);

}