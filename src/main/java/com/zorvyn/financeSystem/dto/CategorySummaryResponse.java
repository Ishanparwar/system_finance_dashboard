package com.zorvyn.financeSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategorySummaryResponse {

    private String category;
    private Double totalAmount;
}