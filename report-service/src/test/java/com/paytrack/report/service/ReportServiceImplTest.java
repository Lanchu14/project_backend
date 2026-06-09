package com.paytrack.report.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import com.paytrack.report.client.ExpenseClient;
import com.paytrack.report.client.UserClient;
import com.paytrack.report.model.ReportResponse;
import com.paytrack.report.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReportServiceImplTest {

    @Mock
    private ExpenseClient expenseClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private ReportServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMonthlyReport() {

        Map<String, Double> category = new HashMap<>();
        category.put("Food", 100.0);

        User user = new User(1L, "test", 5000.0, 4000.0);

        when(expenseClient.getMonthlyExpense("test", 5, 2025)).thenReturn(1000.0);
        when(userClient.getUser("test")).thenReturn(user);
        when(expenseClient.categoryWise("test", 5, 2025)).thenReturn(category);

        ReportResponse response =
                service.monthlyReport("test", 5, 2025);

        assertNotNull(response);
        assertEquals(1000.0, response.getTotalExpense());
    }

    @Test
    void testYearlyReport() {

        User user = new User(1L, "test", 5000.0, 3000.0);

        when(expenseClient.getYearlyExpense("test", 2025)).thenReturn(2000.0);
        when(userClient.getUser("test")).thenReturn(user);

        ReportResponse response =
                service.yearlyReport("test", 2025);

        assertNotNull(response);
    }

    
}