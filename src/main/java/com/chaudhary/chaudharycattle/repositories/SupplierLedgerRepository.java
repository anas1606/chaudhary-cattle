package com.chaudhary.chaudharycattle.repositories;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.SupplierLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SupplierLedgerRepository extends JpaRepository<SupplierLedger,Long> {
    Page<SupplierLedger> findAllByCreatedDateBetween (LocalDate startDate, LocalDate endDate, Pageable page);
    @Query("SELECT sl FROM SupplierLedger sl WHERE createdDate BETWEEN ?1 AND ?2 AND bId = ?3")
    Page<SupplierLedger> findAllByCreatedDateBetweenAndBId (LocalDate startDate, LocalDate endDate, Supplier supplier, Pageable page);
    @Query(value = "SELECT ROUND(SUM(amount),2) FROM SupplierLedger sl WHERE createdDate BETWEEN ?1 AND ?2",nativeQuery = true)
    double sumAmountByCreatedDateBetween (LocalDate startDate, LocalDate endDate);
    @Query(value = "SELECT ROUND(SUM(amount),2) FROM SupplierLedger sl WHERE createdDate BETWEEN ?1 AND ?2 AND bId = ?3",nativeQuery = true)
    double sumAmountByCreatedDateBetweenAndBId (LocalDate startDate, LocalDate endDate, Supplier supplier);
    Long countOfIdByCreatedDateBetween(LocalDate startDate, LocalDate endDate);
}
