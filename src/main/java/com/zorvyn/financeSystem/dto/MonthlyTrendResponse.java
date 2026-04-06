package com.zorvyn.financeSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyTrendResponse {
    private Integer month;
    private Double totalAmount;
}