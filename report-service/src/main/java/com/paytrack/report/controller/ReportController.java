package com.paytrack.report.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.*;

import com.paytrack.report.model.ReportResponse;
import com.paytrack.report.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService service;

    @GetMapping("/monthly")
    public ReportResponse monthly(
            @RequestParam int month,
            @RequestParam int year,
            Principal principal){

        return service.monthlyReport(
                principal.getName(),
                month,
                year);
    }

    @GetMapping("/yearly")
    public ReportResponse yearly(
            @RequestParam int year,
            Principal principal){

        return service.yearlyReport(
                principal.getName(),
                year);
    }


}