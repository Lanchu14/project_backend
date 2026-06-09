package com.paytrack.expense.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paytrack.expense.client.NotificationClient;
import com.paytrack.expense.client.UserClient;
import com.paytrack.expense.entity.Expense;
import com.paytrack.expense.feign.CategoryClient;
import com.paytrack.expense.model.Category;
import com.paytrack.expense.model.User;
import com.paytrack.expense.repository.ExpenseRepository;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private CategoryClient categoryClient;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private ExpenseProducer expenseProducer;

    @InjectMocks
    private ExpenseServiceImpl service;

    private Expense expense;

    @BeforeEach
    void setUp() {

        expense = Expense.builder()
                .id(1L)
                .username("john")
                .category("Food")
                .amount(500.0)
                .description("Lunch")
                .date(LocalDate.now())
                .build();
    }

    /* ================= ADD EXPENSE ================= */

    @Test
    void testAddExpenseSuccess() {

        Category category = new Category();
        category.setName("Food");

        when(categoryClient.getMyCategories("john"))
                .thenReturn(List.of(category));

        when(repository.save(expense))
                .thenReturn(expense);

        User user = new User();
        user.setEmail("john@gmail.com");
        user.setRemainingBudget(5000.0);
        user.setSavingsGoal(1000.0);

        when(userClient.getUser("john"))
                .thenReturn(user);

        Expense saved = service.addExpense(expense);

        assertNotNull(saved);

        verify(repository, times(1))
                .save(expense);

        verify(expenseProducer, times(1))
                .sendMessage(anyString());

        verify(userClient, times(1))
                .deductBudget("john", 500.0);
    }

    @Test
    void testAddExpenseCategoryNotFound() {

        when(categoryClient.getMyCategories("john"))
                .thenReturn(List.of());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.addExpense(expense)
                );

        assertEquals(
                "Category not found",
                exception.getMessage()
        );
    }

    /* ================= GET MY EXPENSES ================= */

    @Test
    void testGetMyExpenses() {

        when(repository.findByUsername("john"))
                .thenReturn(List.of(expense));

        List<Expense> expenses =
                service.getMyExpenses("john");

        assertEquals(1, expenses.size());
    }

    /* ================= MONTHLY ================= */

    @Test
    void testMonthly() {

        when(repository.findByUsernameAndMonthAndYear(
                "john",
                5,
                2026
        )).thenReturn(List.of(expense));

        List<Expense> expenses =
                service.monthly(
                        "john",
                        5,
                        2026
                );

        assertEquals(1, expenses.size());
    }

    /* ================= YEARLY ================= */

    @Test
    void testYearly() {

        when(repository.findByUsernameAndYear(
                "john",
                2026
        )).thenReturn(List.of(expense));

        List<Expense> expenses =
                service.yearly(
                        "john",
                        2026
                );

        assertEquals(1, expenses.size());
    }

    /* ================= MONTHLY TOTAL ================= */

    @Test
    void testGetMonthlyTotal() {

        when(repository.findByUsernameAndMonthAndYear(
                "john",
                5,
                2026
        )).thenReturn(List.of(expense));

        Double total =
                service.getMonthlyTotal(
                        "john",
                        5,
                        2026
                );

        assertEquals(500.0, total);
    }

    /* ================= CATEGORY WISE ================= */

    @Test
    void testCategoryWise() {

        when(repository.findByUsernameAndMonth(
                "john",
                5,
                2026
        )).thenReturn(List.of(expense));

        Map<String, Double> result =
                service.categoryWise(
                        "john",
                        5,
                        2026
                );

        assertEquals(
                500.0,
                result.get("Food")
        );
    }

    /* ================= YEARLY TOTAL ================= */

    @Test
    void testGetYearlyTotal() {

        when(repository.findByUsernameAndYear(
                "john",
                2026
        )).thenReturn(List.of(expense));

        Double total =
                service.getYearlyTotal(
                        "john",
                        2026
                );

        assertEquals(500.0, total);
    }

    /* ================= DELETE EXPENSE ================= */

    @Test
    void testDeleteExpense() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(expense));

        service.deleteExpense(1L, "john");

        verify(repository, times(1))
                .deleteById(1L);

        verify(userClient, times(1))
                .addBudget("john", 500.0);
    }

    @Test
    void testDeleteExpenseNotFound() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> service.deleteExpense(
                                1L,
                                "john"
                        )
                );

        assertEquals(
                "Expense not found",
                exception.getMessage()
        );
    }
}