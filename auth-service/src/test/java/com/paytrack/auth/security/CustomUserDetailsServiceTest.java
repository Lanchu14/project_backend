package com.paytrack.auth.security;

import com.paytrack.auth.entity.User;
import com.paytrack.auth.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsServiceTest {

    @Test
    void loadUser(){

        UserRepository repo = Mockito.mock(UserRepository.class);

        User user = new User();
        user.setUsername("test");

        Mockito.when(repo.findByUsername("test"))
                .thenReturn(Optional.of(user));

        CustomUserDetailsService service =
                new CustomUserDetailsService(repo);

        assertNotNull(service.loadUserByUsername("test"));
    }
}