package com.zorvyn.financeSystem.dto;

import com.zorvyn.financeSystem.model.enums.RecordType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateRecordRequest {

    private Double amount;
    private RecordType type;
    private String category;
    private LocalDate date;
    private String notes;
}