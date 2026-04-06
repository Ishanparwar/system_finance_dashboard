package com.zorvyn.financeSystem.controller;

import com.zorvyn.financeSystem.dto.CategorySummaryResponse;
import com.zorvyn.financeSystem.dto.MonthlyTrendResponse;
import com.zorvyn.financeSystem.dto.SummaryResponse;
import com.zorvyn.financeSystem.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public SummaryResponse getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/category-summary")
    public List<CategorySummaryResponse> getCategorySummary() {
        return dashboardService.getCategorySummary();
    }

    @GetMapping("/monthly-trends")
    public List<MonthlyTrendResponse> getMonthlyTrends() {
        return dashboardService.getMonthlyTrends();
    }
}