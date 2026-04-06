package com.zorvyn.financeSystem.controller;

import com.zorvyn.financeSystem.dto.CreateRecordRequest;
import com.zorvyn.financeSystem.dto.RecordResponse;
import com.zorvyn.financeSystem.dto.UpdateRecordRequest;
import com.zorvyn.financeSystem.exception.BadRequestException;
import com.zorvyn.financeSystem.model.enums.RecordType;
import com.zorvyn.financeSystem.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public RecordResponse createRecord(
            @RequestBody CreateRecordRequest request,
            @RequestParam Long userId) {

        return recordService.createRecord(request, userId);
    }

    @GetMapping
    public List<RecordResponse> getRecords(
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        LocalDate start = null;
        LocalDate end = null;
        try {
            start = startDate != null ? LocalDate.parse(startDate) : null;
            end = endDate != null ? LocalDate.parse(endDate) : null;
        } catch (Exception e) {
            throw new BadRequestException("Invalid date format. Use YYYY-MM-DD");
        }

        return recordService.getAllRecords(type, category, start, end);
    }

    @PutMapping("/{id}")
    public RecordResponse updateRecord(
            @PathVariable Long id,
            @RequestBody UpdateRecordRequest request,
            @RequestParam Long userId) {

        return recordService.updateRecord(id, request, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteRecord(
            @PathVariable Long id,
            @RequestParam Long userId) {

        recordService.deleteRecord(id, userId);
    }
}
