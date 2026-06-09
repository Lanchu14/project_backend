package com.paytrack.report.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ReportDTO {

    private double totalExpense;
    private double budget;
    private Map<String, Double> categoryWise;
}