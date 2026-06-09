package com.paytrack.report.model;

import lombok.Data;

@Data
public class Expense {

    private Long id;
    private Long userId;
    private String category;
    private double amount;
    private String date;
}