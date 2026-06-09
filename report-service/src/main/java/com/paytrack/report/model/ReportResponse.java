package com.paytrack.report.model;

import lombok.Data;
import java.util.Map;

@Data
public class ReportResponse {

    private Double totalExpense;
    private Double budget;
    private Double remaining;
    private Map<String, Double> categoryWise;
}