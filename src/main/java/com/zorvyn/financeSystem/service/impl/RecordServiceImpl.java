package com.zorvyn.financeSystem.service.impl;

import com.zorvyn.financeSystem.dto.CreateRecordRequest;
import com.zorvyn.financeSystem.dto.RecordResponse;
import com.zorvyn.financeSystem.dto.UpdateRecordRequest;
import com.zorvyn.financeSystem.exception.BadRequestException;
import com.zorvyn.financeSystem.exception.ResourceNotFoundException;
import com.zorvyn.financeSystem.exception.UnauthorizedException;
import com.zorvyn.financeSystem.model.FinancialRecord;
import com.zorvyn.financeSystem.model.User;
import com.zorvyn.financeSystem.model.enums.RecordType;
import com.zorvyn.financeSystem.model.enums.Role;
import com.zorvyn.financeSystem.model.enums.Status;
import com.zorvyn.financeSystem.repository.FinancialRecordRepository;
import com.zorvyn.financeSystem.repository.UserRepository;
import com.zorvyn.financeSystem.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    @Override
    public RecordResponse createRecord(CreateRecordRequest request, Long userId) {

        User user = getUser(userId);

        validateActive(user);
        validateNotViewer(user);

        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(user)
                .build();

        return mapToResponse(recordRepository.save(record));
    }

    @Override
    public List<RecordResponse> getAllRecords(
            RecordType type,
            String category,
            LocalDate startDate,
            LocalDate endDate) {

        if(startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BadRequestException("startDate cannot be after endDate");
        }
        List<FinancialRecord> records =
                recordRepository.filterRecords(type, category, startDate, endDate);

        return records.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RecordResponse updateRecord(Long id, UpdateRecordRequest request, Long userId) {

        User user = getUser(userId);
        validateActive(user);
        validateAdminOrAnalyst(user);

        FinancialRecord record = getRecord(id);

        if(request.getAmount() != null) record.setAmount(request.getAmount());
        if(request.getType() != null) record.setType(request.getType());
        if(request.getCategory() != null) record.setCategory(request.getCategory());
        if(request.getDate() != null) record.setDate(request.getDate());
        if(request.getNotes() != null) record.setNotes(request.getNotes());

        return mapToResponse(recordRepository.save(record));
    }

    @Override
    public void deleteRecord(Long id, Long userId) {

        User user = getUser(userId);
        validateAdmin(user);
        validateActive(user);

        FinancialRecord record = getRecord(id);
        if(record.isDeleted()) {
            throw new BadRequestException("Record already deleted");
        }

        record.setDeleted(true);
        recordRepository.save(record);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private FinancialRecord getRecord(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));
        if(record.isDeleted()) {
            throw new ResourceNotFoundException("Record not found");
        }
        return record;
    }

    private void validateActive(User user) {
        if(user.getStatus() == Status.INACTIVE) {
            throw new UnauthorizedException("User is inactive");
        }
    }

    private void validateNotViewer(User user) {
        if(user.getRole() == Role.VIEWER) {
            throw new UnauthorizedException("Access denied");
        }
    }

    private void validateAdmin(User user) {
        if(user.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admin allowed");
        }
    }

    private void validateAdminOrAnalyst(User user) {
        if(user.getRole() == Role.VIEWER) {
            throw new UnauthorizedException("Access denied");
        }
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