package com.zorvyn.financeSystem.repository;

import com.zorvyn.financeSystem.model.FinancialRecord;
import com.zorvyn.financeSystem.model.enums.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    @Query(value = """
    SELECT 
        SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END) AS totalIncome,
        SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END) AS totalExpense
    FROM financial_records
    WHERE is_deleted = false
    """, nativeQuery = true)
    List<Object[]> getSummary();

    @Query("""
SELECT r FROM FinancialRecord r
WHERE r.isDeleted = false
AND (:type IS NULL OR r.type = :type)
AND (:category IS NULL OR LOWER(r.category) = LOWER(:category))
AND (:startDate IS NULL OR r.date >= :startDate)
AND (:endDate IS NULL OR r.date <= :endDate)
""")
    List<FinancialRecord> filterRecords(
            RecordType type,
            String category,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query(value = """
    SELECT category, SUM(amount)
    FROM financial_records
    WHERE is_deleted = false
    GROUP BY category
    """, nativeQuery = true)
    List<Object[]> getCategorySummary();

    @Query(value = """
    SELECT MONTH(date), SUM(amount)
    FROM financial_records
    WHERE is_deleted = false
    GROUP BY MONTH(date)
    ORDER BY MONTH(date)
    """, nativeQuery = true)
    List<Object[]> getMonthlyTrends();
}
