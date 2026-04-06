package com.zorvyn.financeSystem.dto;

import com.zorvyn.financeSystem.model.enums.RecordType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategorySummaryResponse {

    private String category;
    private Double totalAmount;
    private RecordType type;
}