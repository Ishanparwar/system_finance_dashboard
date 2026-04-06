package com.zorvyn.financeSystem.service;

import com.zorvyn.financeSystem.dto.CategorySummaryResponse;
import com.zorvyn.financeSystem.dto.MonthlyTrendResponse;
import com.zorvyn.financeSystem.dto.RecordResponse;
import com.zorvyn.financeSystem.dto.SummaryResponse;

import java.util.List;

public interface DashboardService {
    SummaryResponse getSummary();

    List<CategorySummaryResponse> getCategorySummary();

    public List<MonthlyTrendResponse> getMonthlyTrends(Integer month, Integer year);

    List<RecordResponse> getRecentActivity();
}
