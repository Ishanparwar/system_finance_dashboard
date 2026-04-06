package com.zorvyn.financeSystem.service;

import com.zorvyn.financeSystem.dto.CreateRecordRequest;
import com.zorvyn.financeSystem.dto.RecordResponse;
import com.zorvyn.financeSystem.dto.UpdateRecordRequest;
import com.zorvyn.financeSystem.model.enums.RecordType;

import java.time.LocalDate;
import java.util.List;

public interface RecordService {

    RecordResponse createRecord(CreateRecordRequest request, Long userId);

    List<RecordResponse> getAllRecords(RecordType type, String category,
                                       LocalDate startDate, LocalDate endDate);

    RecordResponse updateRecord(Long id, UpdateRecordRequest request, Long userId);

    void deleteRecord(Long id, Long userId);
}