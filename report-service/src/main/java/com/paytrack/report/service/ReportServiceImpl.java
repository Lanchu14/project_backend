package com.paytrack.report.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.paytrack.report.client.ExpenseClient;
import com.paytrack.report.client.UserClient;
import com.paytrack.report.model.ReportResponse;
import com.paytrack.report.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ExpenseClient expenseClient;
    private final UserClient userClient;

    @Override
    public ReportResponse monthlyReport(
            String username,
            int month,
            int year) {

        Double expense =
                expenseClient.getMonthlyExpense(
                        username,
                        month,
                        year);

        User user =
                userClient.getUser(username);

        Map<String, Double> category =
                expenseClient.categoryWise(
                        username,
                        month,
                        year);

        ReportResponse response =
                new ReportResponse();

        response.setTotalExpense(expense);
        response.setBudget(user.getMonthlyBudget());
        response.setRemaining(user.getRemainingBudget());
        response.setCategoryWise(category);

        return response;
    }

    @Override
    public ReportResponse yearlyReport(
            String username,
            int year) {

        Double expense =
                expenseClient.getYearlyExpense(
                        username,
                        year);

        User user =
                userClient.getUser(username);

        ReportResponse response =
                new ReportResponse();

        response.setTotalExpense(expense);
        response.setBudget(user.getMonthlyBudget());
        response.setRemaining(user.getRemainingBudget());

        return response;
    }


}