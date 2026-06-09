package com.paytrack.expense.service;

import java.util.List;
import java.util.Map;

import com.paytrack.expense.entity.Expense;

public interface ExpenseService {

    // ✅ Add
    Expense addExpense(Expense expense);

    // ✅ Get only logged-in user's data
    List<Expense> getMyExpenses(String username);



    //  Monthly
    List<Expense> monthly(String username, int month, int year);

    //  Yearly
    List<Expense> yearly(String username, int year);

    //  Category-wise
    Map<String, Double> categoryWise(String username, int month, int year);

    //  Totals
    Double getMonthlyTotal(String username, int month, int year);

    Double getYearlyTotal(String username, int year);

    //   DELETE 
    void deleteExpense(Long id, String username);
}