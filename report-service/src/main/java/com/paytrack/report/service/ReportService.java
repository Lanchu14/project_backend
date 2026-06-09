package com.paytrack.report.service;

import com.paytrack.report.model.ReportResponse;

public interface ReportService {

    ReportResponse monthlyReport(
            String username,
            int month,
            int year);

    ReportResponse yearlyReport(
            String username,
            int year);


}