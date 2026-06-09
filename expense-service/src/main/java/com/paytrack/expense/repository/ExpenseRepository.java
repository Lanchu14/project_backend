package com.paytrack.expense.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paytrack.expense.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // ✅ BASIC
    List<Expense> findByUsername(String username);

    // ✅ MONTH + YEAR
    @Query("SELECT e FROM Expense e WHERE e.username = :username AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    List<Expense> findByUsernameAndMonthAndYear(
            String username,
            int month,
            int year);

    // ✅ YEAR ONLY
    @Query("SELECT e FROM Expense e WHERE e.username = :username AND YEAR(e.date) = :year")
    List<Expense> findByUsernameAndYear(
            String username,
            int year);

    // ✅ MONTH (WITH YEAR)
    @Query("SELECT e FROM Expense e WHERE e.username = :username AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    List<Expense> findByUsernameAndMonth(
            String username,
            int month,
            int year);
}