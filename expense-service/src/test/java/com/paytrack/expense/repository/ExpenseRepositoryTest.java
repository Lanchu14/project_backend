package com.paytrack.expense.repository;

import com.paytrack.expense.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository repository;

    @Test
    void shouldSaveExpense() {

        Expense expense = new Expense();
        expense.setAmount(100.0);

        Expense saved = repository.save(expense);

        assertNotNull(saved.getId());
    }

    @Test
    void shouldFindAll() {

        Expense expense = new Expense();
        expense.setAmount(200.0);
        repository.save(expense);

        List<Expense> list = repository.findAll();

        assertFalse(list.isEmpty());
    }
}