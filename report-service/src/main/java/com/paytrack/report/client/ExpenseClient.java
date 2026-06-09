package com.paytrack.report.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(name = "EXPENSE-SERVICE")
public interface ExpenseClient {

    @GetMapping("/expenses/internal/monthly-total")
    Double getMonthlyExpense(
            @RequestParam String username,
            @RequestParam int month,
            @RequestParam int year);

    @GetMapping("/expenses/internal/yearly-total")
    Double getYearlyExpense(
            @RequestParam String username,
            @RequestParam int year);

    @GetMapping("/expenses/internal/category-wise")
    Map<String, Double> categoryWise(
            @RequestParam String username,
            @RequestParam int month,
            @RequestParam int year);
}