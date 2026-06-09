package com.paytrack.expense.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.paytrack.expense.client.NotificationClient;
import com.paytrack.expense.client.UserClient;
import com.paytrack.expense.entity.Expense;
import com.paytrack.expense.feign.CategoryClient;
import com.paytrack.expense.model.Category;
import com.paytrack.expense.model.User;
import com.paytrack.expense.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger logger =
            LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseRepository repository;
    private final UserClient userClient;
    private final CategoryClient categoryClient;
    private final NotificationClient notificationClient;

    // RabbitMQ Producer
    private final ExpenseProducer expenseProducer;

    @Override
    public Expense addExpense(Expense expense) {

        logger.info("Validating category");

        List<Category> categories =
                categoryClient.getMyCategories(
                        expense.getUsername()
                );

        boolean exists = categories.stream()
                .anyMatch(c -> c.getName()
                .equalsIgnoreCase(expense.getCategory()));

        if (!exists) {

            logger.error("Category not found");

            throw new IllegalArgumentException("Category not found");
        }

        logger.info("Saving expense");

        Expense saved = repository.save(expense);

        // RabbitMQ Message Send
        expenseProducer.sendMessage(
                "Expense Added Successfully | Category: "
                        + saved.getCategory()
                        + " | Amount: "
                        + saved.getAmount()
        );

        logger.info("Deducting budget");

        userClient.deductBudget(
                expense.getUsername(),
                expense.getAmount()
        );

        logger.info("Fetching updated user");

        User user = userClient.getUser(
                expense.getUsername()
        );

        if (user.getRemainingBudget() <= user.getSavingsGoal()) {

            logger.warn("Savings goal warning");

            notificationClient.send(
                    user.getEmail(),
                    "Savings Goal Alert",
                    "Warning! Your remaining budget is below savings goal. Remaining: "
                            + user.getRemainingBudget()
            );
        }

        if (user.getRemainingBudget() <= 0) {

            logger.warn("Budget Alert");

            notificationClient.send(
                    user.getEmail(),
                    "Expense is more than Budget Please see through it So that you can save more and enjoy :"
                            + user.getRemainingBudget(),
                    "Thank You "
            );
        }

        return saved;
    }

    @Override
    public List<Expense> getMyExpenses(String username) {

        return repository.findByUsername(username);
    }

    @Override
    public List<Expense> monthly(
            String email,
            int month,
            int year) {

        return repository.findByUsernameAndMonthAndYear(
                email,
                month,
                year
        );
    }

    @Override
    public List<Expense> yearly(
            String username,
            int year) {

        return repository.findByUsernameAndYear(
                username,
                year
        );
    }

    @Override
    public Double getMonthlyTotal(
            String email,
            int month,
            int year) {

        return repository
                .findByUsernameAndMonthAndYear(
                        email,
                        month,
                        year
                )
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    @Override
    public Map<String, Double> categoryWise(
            String email,
            int month,
            int year) {

        List<Expense> expenses =
                repository.findByUsernameAndMonth(
                        email,
                        month,
                        year
                );

        return expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(
                                Expense::getAmount
                        )
                ));
    }

    @Override
    public Double getYearlyTotal(
            String email,
            int year) {

        return repository
                .findByUsernameAndYear(
                        email,
                        year
                )
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    @Override
    public void deleteExpense(
            Long id,
            String username) {

        Expense expense = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expense not found"));

        repository.deleteById(id);

        logger.info("Refunding budget");

        userClient.addBudget(
                expense.getUsername(),
                expense.getAmount()
        );
    }
}