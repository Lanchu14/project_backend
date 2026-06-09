package com.paytrack.expense.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.paytrack.expense.dto.ExpenseRequest;
import com.paytrack.expense.entity.Expense;
import com.paytrack.expense.service.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService service;

    /* ================= ADD EXPENSE ================= */

    @PostMapping
    public Expense addExpense(
            @Valid @RequestBody ExpenseRequest request,
            Principal principal) {

        Expense expense = Expense.builder()
                .username(principal.getName())
                .category(request.getCategory())
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .build();

        return service.addExpense(expense);
    }

    /* ================= GET MY EXPENSES ================= */

    @GetMapping("/my")
    public List<Expense> getMyExpenses(
            Principal principal) {

        return service.getMyExpenses(
                principal.getName()
        );
    }

    /* ================= MONTHLY ================= */

    @GetMapping("/monthly")
    public List<Expense> monthly(
            Principal principal,
            @RequestParam int month,
            @RequestParam int year) {

        return service.monthly(
                principal.getName(),
                month,
                year
        );
    }

    /* ================= YEARLY ================= */

    @GetMapping("/yearly")
    public List<Expense> yearly(
            Principal principal,
            @RequestParam int year) {

        return service.yearly(
                principal.getName(),
                year
        );
    }

    /* ================= INTERNAL ================= */

    @GetMapping("/internal/monthly-total")
    public Double monthlyTotalInternal(
            @RequestParam String username,
            @RequestParam int month,
            @RequestParam int year) {

        return service.getMonthlyTotal(
                username,
                month,
                year
        );
    }

    @GetMapping("/internal/yearly-total")
    public Double yearlyTotalInternal(
            @RequestParam String username,
            @RequestParam int year) {

        return service.getYearlyTotal(
                username,
                year
        );
    }

    @GetMapping("/internal/category-wise")
    public Map<String, Double> categoryWiseInternal(
            @RequestParam String username,
            @RequestParam int month,
            @RequestParam int year) {

        return service.categoryWise(
                username,
                month,
                year
        );
    }

    /* ================= DELETE ================= */

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            Principal principal) {

        service.deleteExpense(
                id,
                principal.getName()
        );
    }
}