package com.paytrack.report.controller;

import com.paytrack.report.model.ReportResponse;
import com.paytrack.report.service.ReportService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

class ReportControllerTest {

    @Test
    void testMonthly() {

        ReportService service = Mockito.mock(ReportService.class);

        ReportController controller =
                new ReportController(service);

        Principal principal = () -> "test";

        Mockito.when(service.monthlyReport("test",5,2025))
                .thenReturn(new ReportResponse());

        ReportResponse response =
                controller.monthly(5,2025,principal);

        assertNotNull(response);
    }

    @Test
    void testYearly() {

        ReportService service = Mockito.mock(ReportService.class);

        ReportController controller =
                new ReportController(service);

        Principal principal = () -> "test";

        Mockito.when(service.yearlyReport("test",2025))
                .thenReturn(new ReportResponse());

        ReportResponse response =
                controller.yearly(2025,principal);

        assertNotNull(response);
    }

  
}