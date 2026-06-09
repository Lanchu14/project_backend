package com.paytrack.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytrack.category.entity.Category;
import com.paytrack.category.security.JwtFilter;
import com.paytrack.category.service.CategoryService;
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
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddCategory() throws Exception {

        Category category = Category.builder()
                .name("Food")
                .type("EXPENSE")
                .build();

        Mockito.when(service.addCategory(Mockito.any()))
                .thenReturn(category);

        mockMvc.perform(post("/categories")
                .principal(() -> "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMyCategories() throws Exception {

        Mockito.when(service.getMyCategories("testuser"))
                .thenReturn(List.of());

        mockMvc.perform(get("/categories/my")
                .principal(() -> "testuser"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAll() throws Exception {

        Mockito.when(service.getAll())
                .thenReturn(List.of());

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isOk());
    }
}