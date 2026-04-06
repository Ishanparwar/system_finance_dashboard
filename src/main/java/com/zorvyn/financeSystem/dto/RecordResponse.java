package com.zorvyn.financeSystem.dto;

import com.zorvyn.financeSystem.model.enums.RecordType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RecordResponse {

    private Long id;
    private Double amount;
    private RecordType type;
    private String category;
    private LocalDate date;
    private String notes;
    private Long createdByUserId;
}
