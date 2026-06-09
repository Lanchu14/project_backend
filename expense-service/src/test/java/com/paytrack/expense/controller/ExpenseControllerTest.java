package com.paytrack.expense.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytrack.expense.entity.Expense;
import com.paytrack.expense.security.JwtFilter;
import com.paytrack.expense.service.ExpenseService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddExpense() throws Exception {

        Expense expense = new Expense();
        expense.setAmount(100.0);

        Mockito.when(service.addExpense(Mockito.any()))
                .thenReturn(expense);

        mockMvc.perform(post("/expenses")
                .principal(() -> "test@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMyExpenses() throws Exception {

        Mockito.when(service.getMyExpenses("test@gmail.com"))
                .thenReturn(List.of());

        mockMvc.perform(get("/expenses/my")
                .principal(() -> "test@gmail.com"))
                .andExpect(status().isOk());
    }

    @Test
    void testMonthly() throws Exception {

        Mockito.when(service.monthly("test@gmail.com", 1, 2025))
                .thenReturn(List.of());

        mockMvc.perform(get("/expenses/monthly")
                .principal(() -> "test@gmail.com")
                .param("month", "1")
                .param("year", "2025"))
                .andExpect(status().isOk());
    }

    @Test
    void testYearly() throws Exception {

        Mockito.when(service.yearly("test@gmail.com", 2025))
                .thenReturn(List.of());

        mockMvc.perform(get("/expenses/yearly")
                .principal(() -> "test@gmail.com")
                .param("year", "2025"))
                .andExpect(status().isOk());
    }

    @Test
    void testMonthlyTotalInternal() throws Exception {

        Mockito.when(service.getMonthlyTotal("test", 1, 2025))
                .thenReturn(100.0);

        mockMvc.perform(get("/expenses/internal/monthly-total")
                .param("username", "test")
                .param("month", "1")
                .param("year", "2025"))
                .andExpect(status().isOk());
    }

    @Test
    void testYearlyTotalInternal() throws Exception {

        Mockito.when(service.getYearlyTotal("test", 2025))
                .thenReturn(200.0);

        mockMvc.perform(get("/expenses/internal/yearly-total")
                .param("username", "test")
                .param("year", "2025"))
                .andExpect(status().isOk());
    }

    @Test
    void testCategoryWiseInternal() throws Exception {

        Mockito.when(service.categoryWise("test", 1, 2025))
                .thenReturn(Map.of());

        mockMvc.perform(get("/expenses/internal/category-wise")
                .param("username", "test")
                .param("month", "1")
                .param("year", "2025"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {

        mockMvc.perform(delete("/expenses/1")
                .principal(() -> "test@gmail.com")) // 🔥 FIX
                .andExpect(status().isOk());
    }
}