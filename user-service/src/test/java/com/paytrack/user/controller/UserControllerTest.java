package com.paytrack.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytrack.user.dto.UserCreateRequest;
import com.paytrack.user.entity.User;
import com.paytrack.user.security.JwtFilter;
import com.paytrack.user.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testGetAllUsers() throws Exception {

        Mockito.when(service.getAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk());
    }


    @Test
    void testGetCurrentUser() throws Exception {

        User user = new User();
        user.setUsername("testuser");

        Mockito.when(service.getByUsername("testuser"))
                .thenReturn(user);

        mockMvc.perform(get("/users/me")
                .principal(() -> "testuser"))
                .andExpect(status().isOk());
    }


    @Test
    void testUpdateProfile() throws Exception {

        User user = new User();
        user.setUsername("testuser");

        Mockito.when(service.updateProfile(
                Mockito.eq("testuser"),
                Mockito.any(User.class)))
                .thenReturn(user);

        mockMvc.perform(put("/users/update")
                .principal(() -> "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }


    @Test
    void testCreateInternal() throws Exception {

        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("testuser");
        request.setEmail("test@gmail.com");

        User user = new User();
        user.setUsername("testuser");

        Mockito.when(service.create(Mockito.any()))
                .thenReturn(user);

        mockMvc.perform(post("/users/internal/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }


    @Test
    void testGetByUsername() throws Exception {

        User user = new User();
        user.setUsername("testuser");

        Mockito.when(service.getByUsername("testuser"))
                .thenReturn(user);

        mockMvc.perform(get("/users/username/testuser"))
                .andExpect(status().isOk());
    }


    @Test
    void testDeductBudget() throws Exception {

        mockMvc.perform(put("/users/internal/deduct")
                .param("username", "testuser")
                .param("amount", "100"))
                .andExpect(status().isOk());
    }


    @Test
    void testAddBudget() throws Exception {

        mockMvc.perform(put("/users/add-budget")
                .param("username", "testuser")
                .param("amount", "200"))
                .andExpect(status().isOk());
    }

}