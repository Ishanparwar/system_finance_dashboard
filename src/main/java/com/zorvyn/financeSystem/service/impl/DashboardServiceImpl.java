package com.zorvyn.financeSystem.service.impl;

import com.zorvyn.financeSystem.dto.CategorySummaryResponse;
import com.zorvyn.financeSystem.dto.MonthlyTrendResponse;
import com.zorvyn.financeSystem.dto.RecordResponse;
import com.zorvyn.financeSystem.dto.SummaryResponse;
import com.zorvyn.financeSystem.model.FinancialRecord;
import com.zorvyn.financeSystem.repository.FinancialRecordRepository;
import com.zorvyn.financeSystem.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final FinancialRecordRepository financialRecordRepository;

    @Override
    public SummaryResponse getSummary() {

        List<Object[]> results = financialRecordRepository.getSummary();

        Object[] row = results.get(0);

        Double income = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
        Double expense = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
        return new SummaryResponse(income, expense, income - expense);
    }
    @Override
    public List<CategorySummaryResponse> getCategorySummary() {

        List<Object[]> results = financialRecordRepository.getCategorySummary();

        return results.stream()
                .map(row -> new CategorySummaryResponse(
                        (String) row[0],
                        ((Number) row[1]).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyTrendResponse> getMonthlyTrends() {

        List<Object[]> results = financialRecordRepository.getMonthlyTrends();

        return results.stream()
                .map(row -> new MonthlyTrendResponse(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecordResponse> getRecentActivity() {

        List<FinancialRecord> records =
                financialRecordRepository.findTop5ByIsDeletedFalseOrderByDateDescIdDesc();

        return records.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    private RecordResponse mapToResponse(FinancialRecord record) {
        return RecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .notes(record.getNotes())
                .createdByUserId(record.getCreatedBy().getId())
                .build();
    }
}
