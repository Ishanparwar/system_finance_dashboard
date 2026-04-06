package com.zorvyn.financeSystem.dto;

import com.zorvyn.financeSystem.model.enums.RecordType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateRecordRequest {

    @NotNull
    private Double amount;

    @NotNull
    private RecordType type;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate date;

    private String notes;
}
